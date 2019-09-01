package com.pinhsiang.fitracker.data

data class Workout (
    val time: Long = System.currentTimeMillis(),
    val motion: String,
    val sets: List<Sets>
)

data class Sets (
    val liftWeight: Int, // Unit : Kg
    val repeats: Int
)
