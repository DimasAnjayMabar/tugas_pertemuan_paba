package com.example.test3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity4 : AppCompatActivity() {
    companion object{
        val dataPegawai = "kirimDataPegawai"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main4)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intentPegawai = intent.getParcelableArrayListExtra<Pegawai>("dataPegawai")

// Memeriksa apakah intentPegawai tidak null dan ada data pegawai
        val isiText = StringBuilder()

// Menggunakan for biasa untuk iterasi melalui data pegawai
        if (intentPegawai != null) {
            for (i in intentPegawai.indices) {
                val pegawai = intentPegawai[i]
                isiText.append("NIP : ${pegawai.nip}\n")
                isiText.append("Nama : ${pegawai.nama}\n")
                isiText.append("Dept : ${pegawai.dept}\n\n")
            }
        }

// Menampilkan isiText yang berisi data semua pegawai pada TextView
        val _showDataPegawai = findViewById<TextView>(R.id.show_data_pegawai)
        _showDataPegawai.text = isiText.toString()


        val _backBtn = findViewById<Button>(R.id.btn_back)
        _backBtn.setOnClickListener {
            val intent = Intent(
                this@MainActivity4,
                MainActivity::class.java
            )
            startActivity(intent)
        }
    }
}