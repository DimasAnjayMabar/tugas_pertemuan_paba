package com.example.pertemuan15

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pertemuan15.database.NoteRoomDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MainActivity : AppCompatActivity() {
    private lateinit var DB : NoteRoomDatabase
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

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)

        fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahData::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).async{
            val note = DB .funNoteDao().selectAll()
            Log.d("data room", note.toString())
        }
    }
}