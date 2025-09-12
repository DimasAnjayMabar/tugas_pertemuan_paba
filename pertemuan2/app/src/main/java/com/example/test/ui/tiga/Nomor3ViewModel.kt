package com.example.test.ui.tiga

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Nomor3ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = generateFullPyramid(3) // Default nilai 3
    }
    val text: LiveData<String> = _text

    // Fungsi untuk membuat piramida segitiga penuh
    fun generateFullPyramid(n: Int): String {
        if (n <= 0) return "Masukkan angka positif"

        val result = StringBuilder()
        val totalWidth = 2 * n - 1 // Lebar maksimum piramida

        for (i in 1..n) {
            val stars = 2 * i - 1 // Jumlah bintang di baris ini
            val spaces = (totalWidth - stars) / 2 // Spasi di kedua sisi

            // Tambahkan spasi di kiri
            for (j in 1..spaces) {
                result.append(" ")
            }

            // Tambahkan bintang
            for (k in 1..stars) {
                result.append("*")
            }

            // Tambahkan spasi di kanan (opsional, untuk symmetry)
            for (j in 1..spaces) {
                result.append(" ")
            }

            result.append("\n") // Pindah ke baris baru
        }

        return result.toString()
    }

    // Fungsi untuk mengupdate piramida dengan nilai baru
    fun updatePyramid(newN: Int) {
        _text.value = generateFullPyramid(newN)
    }
}