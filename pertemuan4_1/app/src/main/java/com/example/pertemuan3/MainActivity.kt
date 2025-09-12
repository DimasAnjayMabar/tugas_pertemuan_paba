package com.example.pertemuan3

import android.os.Bundle
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Tambahan untuk carousel + dots ---
        val horizontalScrollView = findViewById<HorizontalScrollView>(R.id.horizontalScrollView)
        val dotsLayout = findViewById<LinearLayout>(R.id.dotsLayout)

        // konversi dp → px
        val cardWidthPx = (300 * resources.displayMetrics.density + 8 * resources.displayMetrics.density).toInt()

        horizontalScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            // hitung index card terdekat
            val currentCard = (scrollX + cardWidthPx / 2) / cardWidthPx

            // update dots
            for (i in 0 until dotsLayout.childCount) {
                val dot = dotsLayout.getChildAt(i)
                if (i == currentCard) {
                    dot.setBackgroundResource(R.drawable.baseline_circle_24_active)
                } else {
                    dot.setBackgroundResource(R.drawable.baseline_circle_24_inactive)
                }
            }
        }
    }
}
