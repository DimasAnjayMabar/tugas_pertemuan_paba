package com.example.pertemuan15

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pertemuan15.database.Note
import com.example.pertemuan15.database.NoteRoomDatabase
import com.example.pertemuan15.helper.DateHelper.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TambahData : AppCompatActivity() {
    private lateinit var DB: NoteRoomDatabase
    private var noteId: Int = 0
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_data)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DB = NoteRoomDatabase.getDatabase(this)

        val etJudul = findViewById<EditText>(R.id.et_judul)
        val etDeskripsi = findViewById<EditText>(R.id.et_deskripsi)
        val btnTambah = findViewById<Button>(R.id.btn_tambah)
        val btnUpdate = findViewById<Button>(R.id.btn_update)

        // Cek mode
        isEditMode = intent.getIntExtra("addEdit", 0) == 1
        noteId = intent.getIntExtra("noteId", 0)

        if (isEditMode && noteId > 0) {
            btnTambah.visibility = View.GONE
            btnUpdate.visibility = View.VISIBLE
            loadNoteData(etJudul, etDeskripsi)
        } else {
            btnTambah.visibility = View.VISIBLE
            btnUpdate.visibility = View.GONE
        }

        btnTambah.setOnClickListener {
            val judul = etJudul.text.toString().trim()
            val deskripsi = etDeskripsi.text.toString().trim()

            if (judul.isEmpty() || deskripsi.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                DB.funNoteDao().insert(
                    Note(
                        0,
                        judul,
                        deskripsi,
                        getCurrentDate()
                    )
                )

                runOnUiThread {
                    Toast.makeText(this@TambahData, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish() // Langsung kembali ke MainActivity
                }
            }
        }

        btnUpdate.setOnClickListener {
            val judul = etJudul.text.toString().trim()
            val deskripsi = etDeskripsi.text.toString().trim()

            if (judul.isEmpty() || deskripsi.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                DB.funNoteDao().update(
                    judul,
                    deskripsi,
                    noteId
                )

                runOnUiThread {
                    Toast.makeText(this@TambahData, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
                    finish() // Langsung kembali ke MainActivity
                }
            }
        }
    }

    private fun loadNoteData(etJudul: EditText, etDeskripsi: EditText) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val note = DB.funNoteDao().getNoteById(noteId)
                runOnUiThread {
                    if (note != null) {
                        etJudul.setText(note.judul)
                        etDeskripsi.setText(note.deskripsi)
                    } else {
                        Toast.makeText(this@TambahData, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        finish() // Kembali ke MainActivity jika data tidak ditemukan
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@TambahData, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onCancelClick(view: View) {
        finish()
    }
}