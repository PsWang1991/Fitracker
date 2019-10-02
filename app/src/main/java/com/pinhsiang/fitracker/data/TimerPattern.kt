package com.pinhsiang.fitracker.data

import com.pinhsiang.fitracker.ext.secondsIntToTime

data class TimerPattern (
    val exerciseTime: Int,
    val restTime: Int,
    val repeat: Int
)
{
    val displayExerciseTime: String = exerciseTime.secondsIntToTime()
    val displayRestTime: String = restTime.secondsIntToTime()
}