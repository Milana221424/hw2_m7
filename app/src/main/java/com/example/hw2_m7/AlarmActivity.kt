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
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = CoroutineScope(Dispatchers.Main).launch{

            createNotificationChannel()

            binding.btnSelectTime.setOnClickListener { showTimePicker() }
            binding.btnSetAlarm.setOnClickListener { setAlarm() }
            binding.btnCancelAlarm.setOnClickListener { cancelAlarm() }
        }


    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val  intent = Intent(this,AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent,0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this,"Alarm has canceled", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val  intent = Intent(this,AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent,0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )

        Toast.makeText(this,"Alarm set successfully", Toast.LENGTH_SHORT).show()

    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select alarm time")
            .build()

        picker.show(supportFragmentManager, "foxandroid")

        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12) {
                binding.tvAlarmTime.setText( String.format("%02d", picker.hour - 12) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + " PM")

            } else {
                binding.tvAlarmTime.setText( String.format("%02d", picker.hour) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + " AM")

            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "foxandroidReminderChannel"
            val description = "Channel for Alarm"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("foxandroid", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}

/* binding.btnSelectTime.setOnClickListener {
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
                binding.tvAlarmTime.text = "${materialTimerPicker.hour} : ${materialTimerPicker.minute}"
                val delayMills = alarmTime - currentTime

                if (delayMills > 0) {
                    Log.e("ololo", "delayMills: $delayMills", )
                    job = CoroutineScope(Dispatchers.Main).launch {
                        Log.e("ololo", "Delay in proccess, $delayMills")
                        showToast()
                        delay(delayMills)
                        Log.e("ololo", "Delay completed, calling showToast()")
                        showToast()
                    }
                } else {
                    Log.e("ololo", "onCreate: else is working", )
                    Toast.makeText(this, "Выберите время", Toast.LENGTH_SHORT).show()
                }
            }
        }*/