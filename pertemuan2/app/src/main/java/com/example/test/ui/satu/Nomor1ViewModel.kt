package com.example.test.ui.satu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Nomor1ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

    private fun cekPalindrom(teks: String): Boolean {
        val cleaned = teks.replace("\\s".toRegex(), "").lowercase()
        val n = cleaned.length
        for (i in 0 until n / 2) {
            if (cleaned[i] != cleaned[n - i - 1]) {
                return false
            }
        }
        return true
    }

    fun setHasil(teks: String) {
        _text.value = if (cekPalindrom(teks)) {
            "Kata \"$teks\" adalah palindrom"
        } else {
            "Kata \"$teks\" bukan palindrom"
        }
    }
}
