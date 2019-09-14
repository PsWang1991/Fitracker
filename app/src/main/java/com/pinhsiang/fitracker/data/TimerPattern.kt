package com.pinhsiang.fitracker.data

data class TimerPattern (
    val exerciseTime: Int,
    val restTime: Int,
    val repeat: Int
)
{
    val displayExerciseMinutes: String = (exerciseTime / 60).toString()
    val displayExerciseSeconds: String = (exerciseTime % 60).toString()
    val displayRestMinutes: String = (restTime / 60).toString()
    val displayRestSeconds: String = (restTime % 60).toString()
}