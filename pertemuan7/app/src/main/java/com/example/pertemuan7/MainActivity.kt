package com.example.pertemuan7

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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

        val kirimPesan = findViewById<Button>(R.id.btn_kirim_pesan)

        kirimPesan.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra("address", "0895340299650")
                putExtra("sms_body", "halo")
                type = "text/plain"
            }

            if(sendIntent.resolveActivity(packageManager) != null){
                startActivity(Intent.createChooser(sendIntent, "pilih aplikasi"))
            }
        }

        val setAlarm = findViewById<Button>(R.id.btn_set_alarm)

        setAlarm.setOnClickListener {
            val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "tes alarm")
                putExtra(AlarmClock.EXTRA_HOUR, 11)
                putExtra(AlarmClock.EXTRA_MINUTES, 40)
                putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            }
            startActivity(alarmIntent)
        }

        val setTimer = findViewById<Button>(R.id.btn_set_timer)

        setTimer.setOnClickListener {
            val timerIntent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "tes timer")
                putExtra(AlarmClock.EXTRA_LENGTH, 40)
                putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            }
            startActivity(timerIntent)
        }

        val website = findViewById<EditText>(R.id.tv_alamat)
        val keWebsite = findViewById<Button>(R.id.btn_send_website)

        keWebsite.setOnClickListener {
            var webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://" + website.text.toString())
            )

            if(intent.resolveActivity(packageManager) != null){
                startActivity(webIntent)
            }else{
                Toast.makeText(
                    this,
                    "browser tidak ditemukan",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val setEvent = findViewById<Button>(R.id.btn_set_event)

        setEvent.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)

            val datePickerDialog = DatePickerDialog(
                this,
                {_, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)

                    val timePickerDialog = TimePickerDialog(
                        this,
                        {_, selectedHour, selectedMinute ->
                            val selectedDateTime = Calendar.getInstance()
                            selectedDateTime.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)

                            val eventIntent = Intent(Intent.ACTION_INSERT).apply{
                                data = CalendarContract.Events.CONTENT_URI
                                putExtra(CalendarContract.Events.TITLE, "meeting")
                                putExtra(CalendarContract.Events.EVENT_LOCATION, "kantor")
                                putExtra(CalendarContract.Events.DESCRIPTION, "hyalo")
                                putExtra(CalendarContract.Events.ALL_DAY, false)

                                val startTime = selectedDateTime.clone() as Calendar
                                val endTime = selectedDateTime.clone() as Calendar
                                endTime.add(Calendar.HOUR_OF_DAY, 1)
                                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.timeInMillis)
                                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
                            }
                            startActivity(eventIntent)
                        },
                        hour, minutes, true
                    )
                    timePickerDialog.show()
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        val getPhoto = findViewById<Button>(R.id.btn_get_photo)
        val showImage = findViewById<ImageView>(R.id.iv_hasil)

        val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
            bitmap -> if(bitmap != null){
                showImage.setImageBitmap(bitmap)
            }
        }

        getPhoto.setOnClickListener {
            cameraLauncher.launch(null)
        }
    }
}