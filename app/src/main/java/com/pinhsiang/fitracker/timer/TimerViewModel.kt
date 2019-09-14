package com.pinhsiang.fitracker.timer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.data.TimerPattern
import java.util.*

class TimerViewModel(app: Application) : AndroidViewModel(app) {

//    private val timerPattern1 = TimerPattern(exerciseTime = 5, restTime = 3, repeat = 2)
//    private val timerPattern2 = TimerPattern(exerciseTime = 4, restTime = 3, repeat = 1)
//    private val timerPattern3 = TimerPattern(exerciseTime = 5, restTime = 3, repeat = 2)
    private val timerPatternListTemp = mutableListOf<TimerPattern>()

    private val _timerPatternList = MutableLiveData<List<TimerPattern>>()
    val timerPatternList: LiveData<List<TimerPattern>>
        get() = _timerPatternList

    val reviseMode = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val exerciseTime = MutableLiveData<Int>().apply {
        value = 1
    }

    val displayExerciseMinutes = MutableLiveData<Int>().apply {
        value = 0
    }

    val displayExerciseSeconds = MutableLiveData<Int>().apply {
        value = 1
    }

    private val restTime = MutableLiveData<Int>().apply {
        value = 1
    }

    val displayRestMinutes = MutableLiveData<Int>().apply {
        value = 0
    }

    val displayRestSeconds = MutableLiveData<Int>().apply {
        value = 1
    }

    val patternRepeats = MutableLiveData<Int>().apply {
        value = 1
    }

    private var selectedPatternIndex: Int = -1

    private val intervalTimer = object : Timer() {}

    class IntervalTimerTask(private val tp: List<TimerPattern>) : TimerTask() {
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
        if (timerPatternListTemp.isNotEmpty()) {
            val intervalTimerTask = IntervalTimerTask(timerPatternListTemp)
            intervalTimer.schedule(intervalTimerTask, 0, 1000)
        } else {
            Log.i(TAG, "Time set is empty.")
        }
    }

    fun stopTimer() {
        intervalTimer.cancel()
    }

    fun addTimerPattern() {
        exerciseTime.value = displayExerciseMinutes.value!! * 60 + displayExerciseSeconds.value!!
        restTime.value = displayRestMinutes.value!! * 60 + displayRestSeconds.value!!
        timerPatternListTemp.add(TimerPattern(
            exerciseTime = exerciseTime.value!!,
            restTime = restTime.value!!,
            repeat = patternRepeats.value!!
        ))
        _timerPatternList.value = timerPatternListTemp
//        Log.i(TAG, "${setList.value}")
    }

    fun deleteSelectedPattern() {
        if (selectedPatternIndex in 0 until timerPatternListTemp.size) {
            timerPatternListTemp.removeAt(selectedPatternIndex)
            selectedPatternIndex = -1
            _timerPatternList.value = timerPatternListTemp
            reviseModeOff()
        }
    }

    fun reviseSelectedPattern() {
        if (selectedPatternIndex in 0 until timerPatternListTemp.size) {
            exerciseTime.value = displayExerciseMinutes.value!! * 60 + displayExerciseSeconds.value!!
            restTime.value = displayRestMinutes.value!! * 60 + displayRestSeconds.value!!
            timerPatternListTemp[selectedPatternIndex] =
                TimerPattern(
                    exerciseTime = exerciseTime.value!!,
                    restTime = restTime.value!!,
                    repeat = patternRepeats.value!!
                )
            selectedPatternIndex = -1
            _timerPatternList.value = timerPatternListTemp
            reviseModeOff()
        }
    }

    fun reviseModeOn(patternIndex: Int) {
        selectedPatternIndex = patternIndex
        reviseMode.value = true
    }

    private fun reviseModeOff() {
        reviseMode.value = false
    }


    // Plus and minus button
    fun displayExerciseMinutesPlus1() {
        if (displayExerciseMinutes.value!! <= 58) {
            displayExerciseMinutes.value!!.plus(1)
        }
    }

    fun displayExerciseMinutesMinus1() {
        if (displayExerciseMinutes.value!! >= 1) {
            displayExerciseMinutes.value!!.minus(1)
        }
    }

    fun displayExerciseSecondsPlus1() {
        if (displayExerciseSeconds.value!! <= 58) {
            displayExerciseSeconds.value!!.plus(1)
        }
    }

    fun displayExerciseSecondsMinus1() {
        if (displayExerciseSeconds.value!! >= 2) {
            displayExerciseSeconds.value!!.minus(1)
        }
    }

    fun displayRestMinutesPlus1() {
        if (displayRestMinutes.value!! <= 58) {
            displayRestMinutes.value!!.plus(1)
        }
    }

    fun displayRestMinutesMinus1() {
        if (displayRestMinutes.value!! >= 1) {
            displayRestMinutes.value!!.minus(1)
        }
    }

    fun displayRestSecondsPlus1() {
        if (displayRestSeconds.value!! <= 58) {
            displayRestSeconds.value!!.plus(1)
        }
    }

    fun displayRestSecondsMinus1() {
        if (displayRestSeconds.value!! >= 2) {
            displayRestSeconds.value!!.minus(1)
        }
    }

    fun patternRepeatsPlus1() {
        patternRepeats.value!!.plus(1)
    }

    fun patternRepeatsMinus1() {
        if (patternRepeats.value!! >= 2) {
            patternRepeats.value!!.minus(1)
        }
    }
    // Plus and minus button


    // Keeps values of minutes and seconds in legal range.
    fun setDisplayExerciseMinutesTo0() {
        displayExerciseMinutes.value = 0
    }

    fun setDisplayExerciseMinutesTo59() {
        displayExerciseMinutes.value = 59
    }

    fun setDisplayExerciseSecondsTo1() {
        displayExerciseSeconds.value = 1
    }

    fun setDisplayExerciseSecondsTo59() {
        displayExerciseSeconds.value = 59
    }

    fun setDisplayRestMinutesTo0() {
        displayRestMinutes.value = 0
    }

    fun setDisplayRestMinutesTo59() {
        displayRestMinutes.value = 59
    }

    fun setDisplayRestSecondsTo1() {
        displayRestSeconds.value = 1
    }

    fun setDisplayRestSecondsTo59() {
        displayRestSeconds.value = 59
    }

    fun setPatternRepeatsTo1() {
        patternRepeats.value = 1
    }
}