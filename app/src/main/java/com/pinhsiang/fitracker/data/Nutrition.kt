package com.pinhsiang.fitracker.data

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Nutrition(
    @get:Exclude val id: String = "",
    val time: Long = System.currentTimeMillis(),
    val title: String = "",
    val protein: Int = 0,       // Unit : g
    val carbohydrate: Int = 0,  // Unit : g
    val fat: Int =  0           // Unit : g
) : Parcelable