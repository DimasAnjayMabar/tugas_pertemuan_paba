package com.example.pertemuan12

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.squareup.picasso.Picasso

class adapterRecView (private val listWayang : ArrayList<DcWayang>) : RecyclerView.Adapter<adapterRecView.ListViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemCLicked(data: DcWayang)
        fun delData(position: Int)
    }

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val namaWayang = view.findViewById<TextView>(R.id.tv_nama_wayang)
        val karakterWayang = view.findViewById<TextView >(R.id.tv_karakter_wayang)
        val deskripsiWayang = view.findViewById<TextView>(R.id.tv_deskripsi_wayang)
        val gambarWayang = view.findViewById<ImageView>(R.id.iv_wayang)
        val containerWayang = view.findViewById<LinearLayout>(R.id.card)
        val btnDelete = view.findViewById<Button>(R.id.btn_hapus)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listWayang.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val wayang = listWayang[position]
        holder.namaWayang.setText(wayang.nama)
        holder.karakterWayang.setText(wayang.karakter)
        holder.deskripsiWayang.setText(wayang.deskripsi)
        Log.d("TEST", wayang.foto)
        Picasso.get().
            load(wayang.foto).
            resize(100, 100).
            into(holder.gambarWayang)
        holder.containerWayang.setOnClickListener {
            onItemClickCallback.onItemCLicked(listWayang[holder.adapterPosition])
        }
        holder.btnDelete.setOnClickListener {
            onItemClickCallback.delData(position)
        }
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var nama : MutableList<String>
    private lateinit var karakter : MutableList<String>
    private lateinit var deskripsi : MutableList<String>
    private lateinit var gambar : MutableList<String>
    private var arrayListWayang = arrayListOf<DcWayang>()
    private lateinit var rvWayang : RecyclerView

    fun siapkanData(){
        nama = resources.getStringArray(R.array.namaWayang).toMutableList()
        karakter = resources.getStringArray(R.array.karakterUtamaWayang).toMutableList()
        deskripsi = resources.getStringArray(R.array.deskripsiWayang).toMutableList()
        gambar = resources.getStringArray(R.array.gambarWayang).toMutableList()
    }

    fun tambahData(){
        arrayListWayang.clear()
        for(position in nama.indices){
            val data = DcWayang(
                gambar[position],
                nama[position],
                karakter[position],
                deskripsi[position]
            )
            arrayListWayang.add(data)
        }
    }

    fun tampilkanData(){
        rvWayang.layoutManager = LinearLayoutManager(this)
        val adapterWayang = adapterRecView(arrayListWayang)
        rvWayang.adapter = adapterWayang
        adapterWayang.setOnItemClickCallback(object : adapterRecView.OnItemClickCallback{
            override fun onItemCLicked(data: DcWayang) {
                val intent = Intent(this@MainActivity, DetailWayang::class.java)
                intent.putExtra("Kirim Data", data)
                startActivity(intent)
            }

            override fun delData(position: Int) {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Hapus Data")
                    .setMessage("Anda yakin ingin menghapus "+ nama[position] +"?")
                    .setPositiveButton(
                        "Hapus",
                        DialogInterface.OnClickListener{dialog, which ->
                            gambar.removeAt(position)
                            nama.removeAt(position)
                            deskripsi.removeAt(position)
                            karakter.removeAt(position)
                            tambahData()
                            tampilkanData()
                        }
                    )
                    .setNegativeButton(
                        "Batal",
                        DialogInterface.OnClickListener{dialog, which ->
                            Toast.makeText(
                                this@MainActivity,
                                "Data batal dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvWayang = findViewById<RecyclerView>(R.id.rv_wayang)

        rvWayang = findViewById<RecyclerView>(R.id.rv_wayang)
        siapkanData()
        tambahData()
        tampilkanData()


    }
}