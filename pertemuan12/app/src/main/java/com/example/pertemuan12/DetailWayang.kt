package com.example.pertemuan12

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.squareup.picasso.Picasso

class DetailWayang : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_wayang)

        val gambarWayang = findViewById<ImageView>(R.id.iv_detail_gambar_wayang)
        val namaWayang = findViewById<TextView>(R.id.tv_detail_nama_wayang)
        val karakterWayang = findViewById<TextView>(R.id.tv_detail_karakter_wayang)
        val deskripsiWayang = findViewById<TextView>(R.id.tv_detail_deskripsi_wayang)

        val dataIntent = intent.getParcelableExtra<DcWayang>("Kirim Data", DcWayang::class.java)
        if(dataIntent != null){
            Picasso.get()
                .load(dataIntent.foto)
                .into(gambarWayang)
            namaWayang.setText(dataIntent.nama)
            karakterWayang.setText(dataIntent.karakter)
            deskripsiWayang.setText(dataIntent.deskripsi)
        }

        val backBtn = findViewById<Button>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}