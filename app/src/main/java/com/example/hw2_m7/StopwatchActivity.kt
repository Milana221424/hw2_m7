package com.example.hw2_m7

import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hw2_m7.databinding.ActivityStopwatchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopwatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStopwatchBinding

    private var job: Job? = null
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnStart.setOnClickListener {
            startTimer()
        }

        binding.btnStop.setOnClickListener {
            stopTimer()
        }
    }


private fun stopTimer() {
    job?.cancel()
    isTimerRunning = false

    binding.tvTimeHistory.text = binding.tvTime.text
    // resetTimer()
}

/*    private fun resetTimer() {
        tvTime.text = "00:00"
    }*/

private fun startTimer()  {
    if (!isTimerRunning) {
        job = CoroutineScope(Dispatchers.Main).launch {
            var seconds = 0L
            while (true) {
                delay(1000)
                seconds++
                updateTimer(seconds)
            }
        }
        isTimerRunning = true
    } else {
        job?.cancel()
        isTimerRunning = false
    }
}

private fun updateTimer(seconds: Long){
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    val timeString = String.format("%02d:%02d", minutes, remainingSeconds)
    binding.tvTime.text = timeString
}

override fun onDestroy() {
    super.onDestroy()
    job?.cancel()
}

}