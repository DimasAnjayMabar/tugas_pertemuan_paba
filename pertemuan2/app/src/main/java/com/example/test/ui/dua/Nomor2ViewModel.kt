package com.example.test.ui.dua

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Nomor2ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Hitung Kemunculan Karakter"
    }

    private val _characterCount = MutableLiveData<Map<Char, Int>>()
    val characterCount: LiveData<Map<Char, Int>> = _characterCount

    private val _resultText = MutableLiveData<String>()
    val resultText: LiveData<String> = _resultText

    val text: LiveData<String> = _text

    fun countCharacters(input: String): Map<Char, Int> {
        val result = mutableMapOf<Char, Int>()

        for (char in input) {
            result[char] = result.getOrDefault(char, 0) + 1
        }

        return result
    }

    fun processInput(input: String) {
        if (input.isBlank()) {
            _resultText.value = "Masukkan teks terlebih dahulu!"
            _characterCount.value = emptyMap()
            return
        }

        val result = countCharacters(input)
        _characterCount.value = result

        val formattedResult = result.entries.joinToString(
            separator = ", ",
            prefix = "{",
            postfix = "}"
        ) {
            when (it.key) {
                ' ' -> "Spasi=${it.value}"
                '\t' -> "Tab=${it.value}"
                '\n' -> "NewLine=${it.value}"
                else -> "${it.key}=${it.value}"
            }
        }

        _resultText.value = "Hasil: $formattedResult"
    }
}