package com.pinhsiang.fitracker.timer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.data.TimerPattern
import java.util.*

class TimerViewModel(app: Application) : AndroidViewModel(app) {


    private val timerPattern1 = TimerPattern(exerciseTime = 5, restTime = 3, repeat = 2)
    private val timerPattern2 = TimerPattern(exerciseTime = 4, restTime = 3, repeat = 1)
    private val timerPattern3 = TimerPattern(exerciseTime = 5, restTime = 3, repeat = 2)
    private val tp = listOf(timerPattern1, timerPattern2, timerPattern3)

    private val intervalTimer = object : Timer() {}
    private val intervalTimerTask = IntervalTimerTask(tp)

    class IntervalTimerTask(val tp: List<TimerPattern>) : TimerTask() {
        var currentSet = 0
        var repeats = tp[currentSet].repeat
        var timeWork = tp[currentSet].exerciseTime
        var timeRest = tp[currentSet].restTime
        override fun run() {
            if (repeats > 0) {
                if (timeWork > 0) {
                    Log.i(TAG, "動起來 : $timeWork s")
                    timeWork -= 1
                } else {
                    if (timeRest > 0) {
                        Log.i(TAG, "躺好啊，不會? : $timeRest s")
                        timeRest -= 1
                        if (timeRest == 0) {
                            repeats -= 1
                            timeWork = tp[currentSet].exerciseTime
                            timeRest = tp[currentSet].restTime
                            if (repeats == 0) {
                                currentSet += 1
                                if (currentSet < tp.size) {
                                    repeats = tp[currentSet].repeat
                                    timeWork = tp[currentSet].exerciseTime
                                    timeRest = tp[currentSet].restTime
                                } else {
                                    Log.i(TAG, "任務完成")
                                    cancel()
                                }
                            }
                        }
                    }
                }
            }
        }
        override fun cancel(): Boolean {
            Log.i(TAG, "Timer is canceled.")
            return super.cancel()
        }
    }

    fun startTimer() {
        if (tp.isNotEmpty()) {
            intervalTimer.schedule(intervalTimerTask, 0, 1000)
        } else {
            Log.i(TAG, "Time set is empty.")
        }
    }

    fun stopTimer() {
        intervalTimer.cancel()
    }
}