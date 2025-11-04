package com.example.pertemuan12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class adapterRecView (private val listWayang : ArrayList<DcWayang>) : RecyclerView.Adapter<adapterRecView.ListViewHolder>(){
    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val namaWayang = view.findViewById<TextView>(R.id.tv_nama_wayang)
        val karakterWayang = view.findViewById<TextView>(R.id.tv_karakter_wayang)
        val deskripsiWayang = view.findViewById<TextView>(R.id.tv_deskripsi_wayang)
        val gambarWayang = view.findViewById<TextView>(R.id.iv_wayang)
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
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var nama : Array<String>
    private lateinit var karakter : Array<String>
    private lateinit var deskripsi : Array<String>
    private lateinit var gambar : Array <String>
    private var arrayListWayang = arrayListOf<DcWayang>()
    private lateinit var rvWayang : RecyclerView

    fun siapkanData(){
        nama = resources.getStringArray(R.array.namaWayang)
        karakter = resources.getStringArray(R.array.karakterUtamaWayang)
        deskripsi = resources.getStringArray(R.array.deskripsiWayang)
        gambar = resources.getStringArray(R.array.gambarWayang)
    }

    fun tambahData(){
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
        rvWayang.adapter = adapterRecView(arrayListWayang)
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