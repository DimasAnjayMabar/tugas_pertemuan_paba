package com.example.test3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity5 : AppCompatActivity() {
    companion object {
        const val SelectedItem = "extra_selected_item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main5)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _items = findViewById<RadioGroup>(R.id.radio_button_items)
        val _confirm = findViewById<Button>(R.id.button_confirm)

        _confirm.setOnClickListener {
            val selectedRadioButtonId = _items.checkedRadioButtonId
            if (selectedRadioButtonId != -1){
                val _selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                val _selectedItem = _selectedRadioButton.text.toString()

                val result = Intent()
                result.putExtra(SelectedItem, _selectedItem)

                setResult(Activity.RESULT_OK, result)

                finish()
            }
        }
    }
}