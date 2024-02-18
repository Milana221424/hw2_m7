package com.example.hw2_m7

import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2_m7.databinding.ActivityTimerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.text.toIntOrNull
import java.util.TimerTask

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {

            val hours = binding.etHours.text.toString().toIntOrNull() ?: 0
            val minutes = binding.etMinutes.text.toString().toIntOrNull() ?: 0
            val seconds = binding.etSeconds.text.toString().toIntOrNull() ?: 0
            var remainingSeconds = hours * 3600 + minutes * 60 + seconds

            val timer = Timer()
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    job = CoroutineScope(Dispatchers.Main).launch {

                        val hoursLeft = remainingSeconds / 3600
                        val minutesLeft = (remainingSeconds % 3600) / 60
                        val secondsLeft = remainingSeconds % 60
                        this@TimerActivity.runOnUiThread {
                            binding.tvTimer.text =
                                "%02d:%02d:%02d".format(hoursLeft, minutesLeft, secondsLeft)
                        }

                        if (remainingSeconds == 0) {
                            /*this@TimerActivity.runOnUiThread {*/
                                Toast.makeText(
                                    this@TimerActivity,
                                    "Timer is over",
                                    Toast.LENGTH_SHORT).show()

                            timer.cancel()
                        }
                        remainingSeconds--
                    }
                }
            }, 0, 1000)
        }
    }
}

