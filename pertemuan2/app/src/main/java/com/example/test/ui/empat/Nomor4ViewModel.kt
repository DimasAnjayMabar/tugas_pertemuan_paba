package com.example.test.ui.satu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Nomor4ViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Cari Angka yang Hilang"
    }
    val text: LiveData<String> = _text

    // Fungsi untuk mencari angka yang hilang
    fun findMissingNumber(numbers: List<Int>): Int {
        val n = numbers.size + 1
        val expectedSum = n * (n + 1) / 2
        val actualSum = numbers.sum()
        return expectedSum - actualSum
    }

    // Fungsi untuk memvalidasi dan memproses input
    fun processInput(input: String): Result<Int> {
        return try {
            // Bersihkan input dan split menjadi list
            val numbers = input.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
                .sorted()

            // Validasi input
            if (numbers.isEmpty()) {
                return Result.failure(IllegalArgumentException("Input tidak boleh kosong"))
            }

            // Validasi angka harus positif dan unique
            if (numbers.any { it <= 0 }) {
                return Result.failure(IllegalArgumentException("Angka harus positif"))
            }

            if (numbers.toSet().size != numbers.size) {
                return Result.failure(IllegalArgumentException("Angka tidak boleh duplikat"))
            }

            // Cari angka yang hilang
            val missingNumber = findMissingNumber(numbers)
            Result.success(missingNumber)

        } catch (e: NumberFormatException) {
            Result.failure(IllegalArgumentException("Format input tidak valid. Gunakan angka yang dipisahkan koma"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}