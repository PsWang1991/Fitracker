package com.pinhsiang.fitracker.data

data class Workout (
    val year: Int,
    val month: Int,
    val date: Int,
    val motion: String,
    val sets: List<Sets>
)

data class Sets (
    val liftWeight: Int, // Unit : Kg
    val repeats: Int
)
