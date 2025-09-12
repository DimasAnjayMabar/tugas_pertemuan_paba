package com.example.test3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.log

class MainActivity : AppCompatActivity() {


    override fun onStart() {
        super.onStart()
        Log.d("app saya", "on start berjalan")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("app saya", "on restart berjalan")
    }

    override fun onPause() {
        super.onPause()
        Log.d("app saya", "on pause berjalan")
    }

    override fun onResume() {
        super.onResume()
        Log.d("app saya", "on resume berjalan")
    }

    override fun onStop() {
        super.onStop()
        Log.d("app saya", "on stop berjalan")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("app saya", "on destroy berjalan")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var _btnExplisit1 = findViewById<Button>(R.id.btn_explisit_1)

        _btnExplisit1.setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                MainActivity2::class.java
            )

            startActivity(intent)
        }

        val _dataKirim = findViewById<EditText>(R.id.data_kirim)
        val _submit = findViewById<Button>(R.id.btn_explisit_2)

        _submit.setOnClickListener {
            val intentWithData = Intent(
                this@MainActivity,
                MainActivity3::class.java
            ).apply {
                putExtra(MainActivity3.dataTerima, _dataKirim.text.toString())
            }
            startActivity(intentWithData)
        }

        val isiPegawai : ArrayList<Pegawai> = arrayListOf()

        isiPegawai.add(Pegawai(1, "anita", "test"))
        isiPegawai.add(Pegawai(2, "tatik", "marketing"))

        val _btnExplisit3 = findViewById<Button>(R.id.btn_explisit_3)
        _btnExplisit3.setOnClickListener {
            val intentWithData = Intent(
                this@MainActivity,
                MainActivity4::class.java
            ).apply {
                putExtra(MainActivity4.dataPegawai, isiPegawai)
            }
            startActivity(intentWithData)
        }

        Log.d("app saya", "oncreate berjalan")
    }
}