package com.example.project4

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvHour: TextView
    private lateinit var tvMinute: TextView
    private lateinit var tvSecond: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHour = findViewById(R.id.tvHour)
        tvMinute = findViewById(R.id.tvMinute)
        tvSecond = findViewById(R.id.tvSecond)

        // timer: 36 menit 58 detik
        val totalTime = (36 * 60 + 58) * 1000L
        object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / 1000) / 3600
                val minutes = ((millisUntilFinished / 1000) % 3600) / 60
                val seconds = (millisUntilFinished / 1000) % 60

                tvHour.text = String.format("%02d", hours)
                tvMinute.text = String.format("%02d", minutes)
                tvSecond.text = String.format("%02d", seconds)
            }

            override fun onFinish() {
                tvHour.text = "00"
                tvMinute.text = "00"
                tvSecond.text = "00"
                Toast.makeText(this@MainActivity, "Flash Sale selesai!", Toast.LENGTH_SHORT).show()
            }
        }.start()

        // discount button logic
        val all = findViewById<TextView>(R.id.all)
        val ten = findViewById<TextView>(R.id.ten_percent)
        val twenty = findViewById<TextView>(R.id.twenty_percent)
        val thirty = findViewById<TextView>(R.id.thirty_percent)
        val fourty = findViewById<TextView>(R.id.fourty_percent)
        val fifty = findViewById<TextView>(R.id.fifty_percent)

        val buttons = listOf(all, ten, twenty, thirty, fourty, fifty)

        buttons.forEach { btn ->
            btn.setOnClickListener {
                // reset semua
                buttons.forEach { it.isSelected = false }
                // aktifkan yg ditekan
                btn.isSelected = true
            }
        }
    }
}

