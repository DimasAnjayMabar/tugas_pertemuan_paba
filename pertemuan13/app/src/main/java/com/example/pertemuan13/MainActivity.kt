package com.example.pertemuan13

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.R
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.example.pertemuan13.DcDaftarProvinsi
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineExceptionHandler
import java.io.File

class MainActivity : AppCompatActivity() {
    var dataProvinsi = ArrayList<DcDaftarProvinsi>()
    var data : MutableList<Map<String, Any>> = ArrayList()

    //    lateinit var lvAdapter : ArrayAdapter<daftarProvinsi>
    lateinit var lvAdapter : SimpleAdapter
    lateinit var _etProvinsi : EditText
    lateinit var _etIbuKota : EditText
    lateinit var _btnSimpan : Button
    lateinit var _lvData : ListView
    lateinit var _ivUpload : ImageView

    private val CLOUDINARY_CLOUD_NAME = "djozilizv"

    private val UNSIGNED_UPLOAD_PRESET = "preset1"

    private var selectedImageUri : Uri? = null

    private var cameraImageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.example.pertemuan13.R.layout.activity_main)

        val db = Firebase.firestore
        _etProvinsi=findViewById(com.example.pertemuan13.R.id.et_provinsi)
        _etIbuKota=findViewById(com.example.pertemuan13.R.id.et_ibukota)
        _btnSimpan=findViewById(com.example.pertemuan13.R.id.btn_simpan)
        _lvData=findViewById(com.example.pertemuan13.R.id.lv_list_provinsi)
        _ivUpload = findViewById(com.example.pertemuan13.R.id.iv_upload)

//        lvAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, dataProvinsi)
        lvAdapter = SimpleAdapter(
            this,
            data,
            com.example.pertemuan13.R.layout.list_item_with_image,
            arrayOf("Img", "Pro", "Ibu"),
            intArrayOf(com.example.pertemuan13.R.id.imageLogo, com.example.pertemuan13.R.id.text1, com.example.pertemuan13.R.id.text2)
        )

        lvAdapter.setViewBinder { view, data, _ ->
            if (view.id == com.example.pertemuan13.R.id.imageLogo) {
                val imgView = view as ImageView
                val defaultImage = com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark
                if (data is String && data.isNotEmpty()) {
                }else{
                    imgView.setImageResource(defaultImage)
                }
                return@setViewBinder true
            }
            false
        }



        _lvData.setOnItemClickListener { adapterView, view, i, l ->
            _etProvinsi.setText(data[i].get("Pro").toString())
            _etIbuKota.setText(data[i].get("Ibu").toString())
        }

        _lvData.setOnItemLongClickListener { adapterView, view, i, l ->
            val namaProvinsi : String = data[i]["Pro"].toString()
            db.collection("tbProvinsi")
                .document(namaProvinsi)
                .delete()
                .addOnSuccessListener {
                    Log.d("Firebase", "Data Berhasil Dihapus")
                    ReadData(db)
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
            true
        }
        _lvData.adapter = lvAdapter

        _btnSimpan.setOnClickListener {
            TambahData(db, _etProvinsi.text.toString(), _etIbuKota.text.toString())
        }

        ReadData(db)

        val config = mapOf("cloud_name" to CLOUDINARY_CLOUD_NAME, "upload_preset" to UNSIGNED_UPLOAD_PRESET)
        MediaManager.init(this, config)

        _ivUpload.setOnClickListener {
            showImagePickDialog()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.example.pertemuan13.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private val pickImagefromGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){ uri : Uri? ->
        uri?.let {
            selectedImageUri
        }
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()){
            if (it){
                selectedImageUri = cameraImageUri
                _ivUpload.setImageURI(cameraImageUri)
            }
        }

    private fun createImageUri() : Uri? {
        val imageFile = File(
            cacheDir,
            "temp_image_${System.currentTimeMillis()}.jpg"
        )
        return FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            imageFile
        )
    }

    private fun showImagePickDialog(){
        val options = arrayOf("Pilih dari gallery",
            "Ambil foto")
        AlertDialog.Builder(this)
            .setTitle("Pilih gambar")
            .setItems(options){ dialog, which ->
                when(which){
                    0 -> pickImagefromGallery.launch("image/*")
                    1 -> {
                        createImageUri()?.let {uri ->
                            cameraImageUri = uri
                            takePicture.launch(uri)
                        }
                    }
                }
            }
            .show()
    }

    fun TambahData(db: FirebaseFirestore, provinsi : String, ibuKota : String){
        val dataBaru = DcDaftarProvinsi(provinsi, ibuKota)
        db.collection("tbProvinsi")
            .document(_etProvinsi.text.toString())
            .set(dataBaru)
            .addOnSuccessListener {
                dataProvinsi.add(dataBaru)
                lvAdapter.notifyDataSetChanged()
                _etProvinsi.setText("")
                _etIbuKota.setText("")
                ReadData(db)
                Log.d("Firebase", dataBaru.provinsi + " Berhasil Ditambahkan")
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

    fun ReadData(db: FirebaseFirestore){
        db.collection("tbProvinsi")
            .get()
            .addOnSuccessListener {
                    result ->
                data.clear()
                for (item in result) {

//                    val itemData = daftarProvinsi(
//                        item.data.get("provinsi").toString(),
//                        item.data.get("ibuKota").toString()
//                    )
//                    dataProvinsi.add(itemData)

                    val itemdata : MutableMap<String, Any> = HashMap(2)
                    itemdata.put("Pro", item.data.get("provinsi").toString())
                    itemdata.put("Ibu", item.data.get("ibuKota").toString())
                    data.add(itemdata)

                }
                lvAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }
}