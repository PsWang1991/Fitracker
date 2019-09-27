package com.pinhsiang.fitracker.timer

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.FitrackerApplication
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.data.TimerPattern
import com.pinhsiang.fitracker.secondsIntToTime
import com.pinhsiang.fitracker.util.Util.getString
import java.util.*

class TimerViewModel(val app: Application) : AndroidViewModel(app) {

    private val timerPatternListTemp = mutableListOf<TimerPattern>()

    private val _timerPatternList = MutableLiveData<List<TimerPattern>>()
    val timerPatternList: LiveData<List<TimerPattern>>
        get() = _timerPatternList

    private val _addToTimerPatternList = MutableLiveData<Boolean>()
    val addToTimerPatternList: LiveData<Boolean>
        get() = _addToTimerPatternList

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

    // A flag for showing count down timer layout
    val timerStart = MutableLiveData<Boolean>().apply {
        value = false
    }

    val intervalTimerStatus = MutableLiveData<String>().apply {
        value = getString(R.string.exercise)
    }

    val intervalTimerTime = MutableLiveData<String>().apply {
        value = "00:00"
    }

    private var selectedPatternIndex: Int = -1

    lateinit var intervalTimer: Timer

    var clockSound = MediaPlayer
        .create(FitrackerApplication.appContext, R.raw.household_clock_grandfather_door_open)

    var dingSound = MediaPlayer
        .create(FitrackerApplication.appContext, R.raw.ding_sound_effect)

    inner class IntervalTimerTask(private val tp: List<TimerPattern>) : TimerTask() {
        var currentSet = 0
        var repeats = tp[currentSet].repeat
        var timeWork = tp[currentSet].exerciseTime
        var timeRest = tp[currentSet].restTime
        override fun run() {
            if (repeats > 0) {
                if (timeWork > 0) {
                    intervalTimerStatus.postValue(getString(R.string.exercise))
                    intervalTimerTime.postValue(timeWork.secondsIntToTime())
                    when (timeWork) {
                        in 2..3 -> clockSound.start()
                        1 -> dingSound.start()
                    }
                    timeWork -= 1
                } else {
                    if (timeRest > 0) {
                        intervalTimerStatus.postValue(getString(R.string.rest))
                        intervalTimerTime.postValue(timeRest.secondsIntToTime())

                        // Alarms user that the next interval is coming.
                        when (timeRest) {
                            in 2..3 -> clockSound.start()
                            1 -> dingSound.start()
                        }
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
            timerStart.postValue(false)
            return super.cancel()
        }
    }

    fun startTimer() {
        if (timerPatternListTemp.isNotEmpty()) {
            val intervalTimerTask = IntervalTimerTask(timerPatternListTemp)
            intervalTimer = object : Timer() {}
            Log.i(TAG, "intervalTimer = $intervalTimer")
            timerStart.value = true
            intervalTimer.schedule(intervalTimerTask, 0, 1000)
        } else {
            Log.i(TAG, "Time set is empty.")
            Toast.makeText(
                FitrackerApplication.appContext,
                "Timer pattern must have at least 1 set.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun stopTimer() {
        intervalTimer.cancel()
        timerStart.value = false
        Log.i(TAG, "intervalTimer = $intervalTimer")
    }

    fun addTimerPattern() {
        exerciseTime.value = displayExerciseMinutes.value!! * 60 + displayExerciseSeconds.value!!
        restTime.value = displayRestMinutes.value!! * 60 + displayRestSeconds.value!!
        if (exerciseTime.value!! > 0 && restTime.value!! > 0 && patternRepeats.value!! > 0) {
            timerPatternListTemp.add(
                TimerPattern(
                    exerciseTime = exerciseTime.value!!,
                    restTime = restTime.value!!,
                    repeat = patternRepeats.value!!
                )
            )
            _timerPatternList.value = timerPatternListTemp
            _addToTimerPatternList.value = true
        } else {
            Toast.makeText(
                FitrackerApplication.appContext,
                "Exercise time, Rest time and repeats must be larger than 0.",
                Toast.LENGTH_SHORT
            ).show()
        }
//        Log.i(TAG, "${setList.value}")
    }

    fun setAddToTimerPatternListFalse() {
        _addToTimerPatternList.value = false
    }

    fun endOfTimerPatternList(): Int {
        return timerPatternListTemp.size - 1
    }

    fun deleteSelectedPattern() {
        if (selectedPatternIndex in 0 until timerPatternListTemp.size) {
            timerPatternListTemp.removeAt(selectedPatternIndex)
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
            _timerPatternList.value = timerPatternListTemp
            reviseModeOff()
        }
    }

    fun reviseModeOn(patternIndex: Int) {
        selectedPatternIndex = patternIndex
        reviseMode.value = true
    }

    fun reviseModeOff() {
        selectedPatternIndex = -1
        reviseMode.value = false
    }


    // Plus and minus button
    fun displayExerciseMinutesPlus1() {
        if (displayExerciseMinutes.value!! <= 58) {
            displayExerciseMinutes.value = displayExerciseMinutes.value!!.plus(1)
        }
//        Log.i(TAG, "displayExerciseMinutes = ${displayExerciseMinutes.value}")
    }

    fun displayExerciseMinutesMinus1() {
        if (displayExerciseSeconds.value!! >= 1) {
            if (displayExerciseMinutes.value!! >= 1) {
                displayExerciseMinutes.value = displayExerciseMinutes.value!!.minus(1)
            }
        } else {
            if (displayExerciseMinutes.value!! >= 2) {
                displayExerciseMinutes.value = displayExerciseMinutes.value!!.minus(1)
            }
        }
//        Log.i(TAG, "displayExerciseMinutes = ${displayExerciseMinutes.value}")
    }

    fun displayExerciseSecondsPlus1() {
        if (displayExerciseSeconds.value!! <= 58) {
            displayExerciseSeconds.value = displayExerciseSeconds.value!!.plus(1)
        }
//        Log.i(TAG, "displayExerciseSeconds = ${displayExerciseSeconds.value}")
    }

    fun displayExerciseSecondsMinus1() {
        if (displayExerciseMinutes.value!! >= 1) {
            if (displayExerciseSeconds.value!! >= 1) {
                displayExerciseSeconds.value = displayExerciseSeconds.value!!.minus(1)
            }
        } else {
            if (displayExerciseSeconds.value!! >= 2) {
                displayExerciseSeconds.value = displayExerciseSeconds.value!!.minus(1)
            }
        }
//        Log.i(TAG, "displayExerciseSeconds = ${displayExerciseSeconds.value}")
    }

    fun displayRestMinutesPlus1() {
        if (displayRestMinutes.value!! <= 58) {
            displayRestMinutes.value = displayRestMinutes.value!!.plus(1)
        }
//        Log.i(TAG, "displayRestMinutes = ${displayRestMinutes.value}")
    }

    fun displayRestMinutesMinus1() {
        if (displayRestSeconds.value!! >= 1) {
            if (displayRestMinutes.value!! >= 1) {
                displayRestMinutes.value = displayRestMinutes.value!!.minus(1)
            }
        } else {
            if (displayRestMinutes.value!! >= 2) {
                displayRestMinutes.value = displayRestMinutes.value!!.minus(1)
            }
        }
//        Log.i(TAG, "displayRestMinutes = ${displayRestMinutes.value}")
    }

    fun displayRestSecondsPlus1() {
        if (displayRestSeconds.value!! <= 58) {
            displayRestSeconds.value = displayRestSeconds.value!!.plus(1)
        }
//        Log.i(TAG, "displayRestSeconds = ${displayRestSeconds.value}")
    }

    fun displayRestSecondsMinus1() {
        if (displayRestMinutes.value!! >= 1) {
            if (displayRestSeconds.value!! >= 1) {
                displayRestSeconds.value = displayRestSeconds.value!!.minus(1)
            }
        } else {
            if (displayRestSeconds.value!! >= 2) {
                displayRestSeconds.value = displayRestSeconds.value!!.minus(1)
            }
        }
//        Log.i(TAG, "displayRestSeconds = ${displayRestSeconds.value}")
    }

    fun patternRepeatsPlus1() {
        patternRepeats.value = patternRepeats.value!!.plus(1)
//        Log.i(TAG, "patternRepeats = ${patternRepeats.value}")
    }

    fun patternRepeatsMinus1() {
        if (patternRepeats.value!! >= 2) {
            patternRepeats.value = patternRepeats.value!!.minus(1)
        }
//        Log.i(TAG, "patternRepeats = ${patternRepeats.value}")
    }


    // Keeps values of minutes and seconds in legal range.
    fun setDisplayExerciseMinutesTo1() {
        displayExerciseMinutes.value = 1
    }

    fun setDisplayExerciseMinutesTo0() {
        displayExerciseMinutes.value = 0
    }

    fun setDisplayExerciseMinutesTo59() {
        displayExerciseMinutes.value = 59
    }

    fun setDisplayExerciseSecondsTo1() {
        displayExerciseSeconds.value = 1
    }

    fun setDisplayExerciseSecondsTo0() {
        displayExerciseSeconds.value = 0
    }

    fun setDisplayExerciseSecondsTo59() {
        displayExerciseSeconds.value = 59
    }

    fun setDisplayRestMinutesTo1() {
        displayRestMinutes.value = 1
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

    fun setDisplayRestSecondsTo0() {
        displayRestSeconds.value = 0
    }

    fun setDisplayRestSecondsTo59() {
        displayRestSeconds.value = 59
    }

    fun setPatternRepeatsTo1() {
        patternRepeats.value = 1
    }
}