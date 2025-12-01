package com.example.pertemuan15

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pertemuan15.database.Note
import com.example.pertemuan15.database.NoteRoomDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class adapterNote(private val listNotes: MutableList<Note>) :
    RecyclerView.Adapter<adapterNote.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onEditClick(note: Note)
        fun onDeleteClick(note: Note)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_recycler, parent, false)
        return ListViewHolder(view)
    }

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJudul: TextView = view.findViewById(R.id.judul_note)
        val tvTanggal: TextView = view.findViewById(R.id.tanggal_note)
        val tvDeskripsi: TextView = view.findViewById(R.id.judul_deskripsi)
        val btnEdit: ImageView = view.findViewById(R.id.btn_edit)
        val btnDelete: ImageView = view.findViewById(R.id.btn_delete)
    }

    override fun getItemCount(): Int = listNotes.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val note = listNotes[position]

        holder.tvJudul.text = note.judul
        holder.tvDeskripsi.text = note.deskripsi
        holder.tvTanggal.text = note.tanggal

        holder.btnEdit.setOnClickListener {
            onItemClickCallback.onEditClick(note)
        }

        holder.btnDelete.setOnClickListener {
            onItemClickCallback.onDeleteClick(note)
        }
    }

    fun updateData(newList: List<Note>) {
        listNotes.clear()
        listNotes.addAll(newList)
        notifyDataSetChanged()
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var DB: NoteRoomDatabase
    private lateinit var adapterN: adapterNote
    private var arNote: MutableList<Note> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DB = NoteRoomDatabase.getDatabase(this)

        // Setup RecyclerView
        adapterN = adapterNote(arNote)
        val rvNotes = findViewById<RecyclerView>(R.id.rv_notes)
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = adapterN

        // Setup FAB
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val intent = Intent(this, TambahData::class.java)
            intent.putExtra("addEdit", 0)
            startActivity(intent)
        }

        // Setup callback untuk adapter
        adapterN.setOnItemClickCallback(object : adapterNote.OnItemClickCallback {
            override fun onEditClick(note: Note) {
                editNote(note)
            }

            override fun onDeleteClick(note: Note) {
                showDeleteConfirmation(note)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadNotes() // PASTIKAN dipanggil di onResume
    }

    private fun loadNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val notes = DB.funNoteDao().selectAll()
                Log.d("MainActivity", "Data loaded: ${notes.size} items")

                runOnUiThread {
                    adapterN.updateData(notes)

                    // Debug: Cek data
                    if (notes.isEmpty()) {
                        Log.d("MainActivity", "No data found in database")
                    } else {
                        notes.forEach { note ->
                            Log.d("MainActivity", "Note: ${note.id} - ${note.judul}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading notes", e)
                runOnUiThread {
                    // Tampilkan error ke user
                }
            }
        }
    }

    private fun editNote(note: Note) {
        val intent = Intent(this, TambahData::class.java)
        intent.putExtra("noteId", note.id)
        intent.putExtra("addEdit", 1)
        startActivity(intent)
    }

    private fun showDeleteConfirmation(note: Note) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Hapus Catatan")
            .setMessage("Apakah Anda yakin ingin menghapus '${note.judul}'?")
            .setPositiveButton("Hapus") { _, _ ->
                deleteNote(note)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                DB.funNoteDao().delete(note)

                runOnUiThread {
                    loadNotes() // Refresh data

                    // Tampilkan snackbar
                    com.google.android.material.snackbar.Snackbar.make(
                        findViewById(R.id.main),
                        "Catatan '${note.judul}' dihapus",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error deleting note", e)
                runOnUiThread {
                    com.google.android.material.snackbar.Snackbar.make(
                        findViewById(R.id.main),
                        "Gagal menghapus catatan",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}