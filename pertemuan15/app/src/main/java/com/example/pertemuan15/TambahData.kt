package com.example.pertemuan15

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pertemuan15.database.Note
import com.example.pertemuan15.database.NoteRoomDatabase
import com.example.pertemuan15.helper.DateHelper.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TambahData : AppCompatActivity() {
    val DB : NoteRoomDatabase = NoteRoomDatabase.getDatabase(this)
    var tanggal : String = getCurrentDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etJudul = findViewById<EditText>(R.id.et_judul)
        val etDeskripsi = findViewById<EditText>(R.id.et_deskripsi)
        val btnTambah = findViewById<Button>(R.id.btn_tambah)
        val btnUpdate = findViewById<Button>(R.id.btn_update)

        btnTambah.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.funNoteDao().insert(
                    Note(
                        0,
                        etJudul.text.toString(),
                        etDeskripsi.text.toString(),
                        tanggal
                    )
                )
            }
        }
    }
}