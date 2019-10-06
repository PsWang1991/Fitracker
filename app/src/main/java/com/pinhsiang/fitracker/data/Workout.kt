package com.pinhsiang.fitracker.data

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Workout(

    @get:Exclude var id: String = "",
    val time: Long = System.currentTimeMillis(),
    val motion: String = "",
    val maxWeight: Int = 0,
    val sets: List<Sets>? = mutableListOf()

) : Parcelable

@Parcelize
data class Sets(

    var liftWeight: Int = 0, // Unit : Kg
    var repeats: Int = 0

) : Parcelable