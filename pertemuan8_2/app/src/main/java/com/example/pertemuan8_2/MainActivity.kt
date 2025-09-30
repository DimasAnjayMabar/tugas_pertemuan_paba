package com.example.pertemuan8_2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    private fun replaceFragment(fragment: Fragment, num1: Double, num2: Double, result: Double, operation: String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val dataKirim = Bundle().apply {
            putString("num1", num1.toString())
            putString("num2", num2.toString())
            putString("result", result.toString())
            putString("operation", operation)
        }
        fragment.arguments = dataKirim

        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
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

        val plus = findViewById<Button>(R.id.btn_plus)
        val minus = findViewById<Button>(R.id.btn_minus)
        val times = findViewById<Button>(R.id.btn_times)
        val divide = findViewById<Button>(R.id.btn_divide)
        val num1 = findViewById<EditText>(R.id.et_num_1).text.toString().toDoubleOrNull()
        val num2 = findViewById<EditText>(R.id.et_num_2).text.toString().toDoubleOrNull()

        if (num1 != null && num2 != null) {
            plus.setOnClickListener {
                val result = num1 + num2
                replaceFragment(fSatu(), num1, num2, result, "+")
            }

            minus.setOnClickListener {
                val result = num1 - num2
                replaceFragment(fSatu(), num1, num2, result, "-")
            }

            times.setOnClickListener {
                val result = num1 * num2
                replaceFragment(fSatu(), num1, num2, result, "*")
            }

            divide.setOnClickListener {
                if (num2 != 0.0) {
                    val result = num1 / num2
                    replaceFragment(fSatu(), num1, num2, result, "/")
                } else {
                    // Handle division by zero if needed
                    Toast.makeText(this, "Cannot divide by zero!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle invalid input
            Toast.makeText(this, "Please enter valid numbers!", Toast.LENGTH_SHORT).show()
        }
    }
}