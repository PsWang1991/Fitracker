package com.pinhsiang.fitracker.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Workout (
    val time: Long = System.currentTimeMillis(),
    val motion: String,
    val sets: List<Sets>? = mutableListOf()
) : Parcelable

@Parcelize
data class Sets (
    var liftWeight: Int, // Unit : Kg
    var repeats: Int
) : Parcelable
