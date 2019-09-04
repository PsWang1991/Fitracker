package com.pinhsiang.fitracker.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Nutrition(
    val time: Long = System.currentTimeMillis(),
    val title: String,
    val protein: Int,       // Unit : g
    val carbohydrate: Int,  // Unit : g
    val fat: Int            // Unit : g
) : Parcelable