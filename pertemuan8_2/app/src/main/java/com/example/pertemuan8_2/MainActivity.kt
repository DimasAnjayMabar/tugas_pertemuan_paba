package com.example.pertemuan8_2

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), fSatu.OnOperationSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // tampilkan fragment tombol (fSatu)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_1, fSatu())
            .commit()
    }

    override fun onOperationSelected(operation: String) {
        val num1 = findViewById<EditText>(R.id.et_num_1).text.toString().toDoubleOrNull()
        val num2 = findViewById<EditText>(R.id.et_num_2).text.toString().toDoubleOrNull()

        if (num1 == null || num2 == null) {
            Toast.makeText(this, "Please enter valid numbers!", Toast.LENGTH_SHORT).show()
            return
        }

        val result = when (operation) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "x" -> num1 * num2
            "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN
            else -> 0.0
        }

        replaceFragment(fDua(), num1, num2, result, operation)
    }

    fun resetInputs(){
        findViewById<EditText>(R.id.et_num_1).setText(null)
        findViewById<EditText>(R.id.et_num_2).setText(null)
    }

    private fun replaceFragment(fragment: Fragment, num1: Double, num2: Double, result: Double, operation: String) {
        val dataKirim = Bundle().apply {
            putString("num1", num1.toString())
            putString("num2", num2.toString())
            putString("result", result.toString())
            putString("operation", operation)
        }
        fragment.arguments = dataKirim

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_1, fragment)
            .addToBackStack(null)
            .commit()
    }
}
