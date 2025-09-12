package com.example.test3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity3 : AppCompatActivity() {
    companion object{
        const val dataTerima = "extra_dataTerima"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val data = intent.getStringExtra(dataTerima)
        val _showData = findViewById<TextView>(R.id.show_data)
        _showData.text = data?.toString()

        var _btnBack = findViewById<Button>(R.id.btn_back)
        _btnBack.setOnClickListener {
            val intent = Intent(
                this@MainActivity3,
                MainActivity::class.java
            )

            startActivity(intent)
        }
    }
}