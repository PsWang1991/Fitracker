package com.pinhsiang.fitracker.timer

import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinhsiang.fitracker.FitrackerApplication
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.TimerPattern
import com.pinhsiang.fitracker.ext.secondsIntToTime
import com.pinhsiang.fitracker.util.Util.getString
import java.util.*

class TimerViewModel : ViewModel() {

    private val timerPatternListTemp = mutableListOf<TimerPattern>()

    private val _timerPatternList = MutableLiveData<List<TimerPattern>>()
    val timerPatternList: LiveData<List<TimerPattern>>
        get() = _timerPatternList

    private val _addNewPattern = MutableLiveData<Boolean>()
    val addNewPattern: LiveData<Boolean>
        get() = _addNewPattern

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

    var clockSound: MediaPlayer = MediaPlayer
        .create(FitrackerApplication.appContext, R.raw.household_clock_grandfather_door_open)

    var dingSound: MediaPlayer = MediaPlayer
        .create(FitrackerApplication.appContext, R.raw.ding_sound_effect)

    inner class IntervalTimerTask(private val timePattern: List<TimerPattern>) : TimerTask() {

        var currentSet = 0
        var repeats = timePattern[currentSet].repeat
        var timeWork = timePattern[currentSet].exerciseTime
        var timeRest = timePattern[currentSet].restTime

        private fun playSound(second: Int) {

            when (second) {
                in 2..3 -> clockSound.start()
                1 -> dingSound.start()
            }
        }

        private fun showCurrentStatusAndTime() {

            if (timeWork > 0) {

                intervalTimerStatus.postValue(getString(R.string.exercise))
                intervalTimerTime.postValue(timeWork.secondsIntToTime())

            } else {

                intervalTimerStatus.postValue(getString(R.string.rest))
                intervalTimerTime.postValue(timeRest.secondsIntToTime())
            }
        }

        override fun run() {

            if (currentSet == timePattern.size) {
                showCurrentStatusAndTime()
                cancel()
            }

            if (timeWork > 0) {

                showCurrentStatusAndTime()
                timeWork -= 1

                // Alarms user that the next interval is coming.
                playSound(timeWork)

            } else {

                if (timeRest > 0) {

                    showCurrentStatusAndTime()
                    timeRest -= 1

                    // Alarms user that the next interval is coming.
                    playSound(timeRest)

                    if (timeRest == 0) {

                        repeats -= 1
                        timeWork = timePattern[currentSet].exerciseTime
                        timeRest = timePattern[currentSet].restTime

                        if (repeats == 0) {

                            currentSet += 1

                            if (currentSet < timePattern.size) {

                                repeats = timePattern[currentSet].repeat
                                timeWork = timePattern[currentSet].exerciseTime
                                timeRest = timePattern[currentSet].restTime
                            }
                        }
                    }
                }
            }
        }

        override fun cancel(): Boolean {
            timerStart.postValue(false)
            return super.cancel()
        }
    }

    fun startTimer() {

        if (timerPatternListTemp.isNotEmpty()) {

            val intervalTimerTask = IntervalTimerTask(timerPatternListTemp)
            intervalTimer = object : Timer() {}
            intervalTimer.schedule(intervalTimerTask, 0, 1000)
            timerStart.value = true

        } else {

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
            _addNewPattern.value = true

        } else {

            Toast.makeText(
                FitrackerApplication.appContext,
                "Exercise time, Rest time and repeats must be larger than 0.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun addNewPatternDOne() {
        _addNewPattern.value = false
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

    fun displayExerciseMinutesPlus1() {
        if (displayExerciseMinutes.value!! <= 58) {
            displayExerciseMinutes.value = displayExerciseMinutes.value!!.plus(1)
        }
    }

    fun displayExerciseMinutesMinus1() {

        when {
            displayExerciseSeconds.value!! >= 1 ->
                if (displayExerciseMinutes.value!! >= 1) {
                    displayExerciseMinutes.value = displayExerciseMinutes.value!!.minus(1)
                }

            else ->
                if (displayExerciseMinutes.value!! >= 2) {
                    displayExerciseMinutes.value = displayExerciseMinutes.value!!.minus(1)
                }
        }
    }

    fun displayExerciseSecondsPlus1() {
        if (displayExerciseSeconds.value!! <= 58) {
            displayExerciseSeconds.value = displayExerciseSeconds.value!!.plus(1)
        }
    }

    fun displayExerciseSecondsMinus1() {

        when {
            displayExerciseMinutes.value!! >= 1 ->
                if (displayExerciseSeconds.value!! >= 1) {
                    displayExerciseSeconds.value = displayExerciseSeconds.value!!.minus(1)
                }

            else ->
                if (displayExerciseSeconds.value!! >= 2) {
                    displayExerciseSeconds.value = displayExerciseSeconds.value!!.minus(1)
                }
        }
    }

    fun displayRestMinutesPlus1() {
        if (displayRestMinutes.value!! <= 58) displayRestMinutes.value = displayRestMinutes.value!!.plus(1)
    }

    fun displayRestMinutesMinus1() {

        when {
            displayRestSeconds.value!! >= 1 ->
                if (displayRestMinutes.value!! >= 1) displayRestMinutes.value = displayRestMinutes.value!!.minus(1)

            else ->
                if (displayRestMinutes.value!! >= 2) displayRestMinutes.value = displayRestMinutes.value!!.minus(1)
        }
    }

    fun displayRestSecondsPlus1() {
        if (displayRestSeconds.value!! <= 58) displayRestSeconds.value = displayRestSeconds.value!!.plus(1)
    }

    fun displayRestSecondsMinus1() {

        when {
            displayRestMinutes.value!! >= 1 ->
                if (displayRestSeconds.value!! >= 1) displayRestSeconds.value = displayRestSeconds.value!!.minus(1)

            else ->
                if (displayRestSeconds.value!! >= 2) displayRestSeconds.value = displayRestSeconds.value!!.minus(1)
        }
    }

    fun patternRepeatsPlus1() {
        patternRepeats.value = patternRepeats.value!!.plus(1)
    }

    fun patternRepeatsMinus1() {
        if (patternRepeats.value!! >= 2) patternRepeats.value = patternRepeats.value!!.minus(1)
    }


    /**
     *  Keeps values of minutes and seconds in legal range.
     */
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