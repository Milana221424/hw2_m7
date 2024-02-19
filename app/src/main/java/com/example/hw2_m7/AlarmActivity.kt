package com.example.hw2_m7

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.example.hw2_m7.databinding.ActivityAlarmBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmBinding
    private var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectTime.setOnClickListener {
            val materialTimerPicker =
                MaterialTimePicker.Builder().setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("SELECT TIME")
                    .build()

            materialTimerPicker.show(supportFragmentManager, "tag")

            materialTimerPicker.addOnPositiveButtonClickListener {

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, materialTimerPicker.hour)
                calendar.set(Calendar.MINUTE, materialTimerPicker.minute)
                calendar.set(Calendar.SECOND, 0)

                val currentTime = Calendar.getInstance().timeInMillis
                val alarmTime = calendar.timeInMillis
                binding.tvAlarmTime.text =
                    "${materialTimerPicker.hour} : ${materialTimerPicker.minute}"
                val delayMills = alarmTime - currentTime

                if (delayMills > 0) {
                    Log.e("ololo", "delayMills: $delayMills")
                    job = CoroutineScope(Dispatchers.Main).launch {
                        Log.e("ololo", "Delay in proccess, $delayMills")
                        delay(delayMills)
                        Log.e("ololo", "Delay completed, calling showToast()")
                        showToast()
                    }
                } else {
                    Log.e("ololo", "onCreate: else is working")
                    Toast.makeText(this, "Выберите время", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showToast() {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this@AlarmActivity, "Будильник сработал", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
