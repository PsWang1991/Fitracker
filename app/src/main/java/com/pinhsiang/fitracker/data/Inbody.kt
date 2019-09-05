package com.pinhsiang.fitracker.data

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Inbody (
    @get:Exclude var id: String = "",
    val time: Long = System.currentTimeMillis(),
    val bodyWeight: Float = 0.0F,       // Unit : Kg
    val bodyFat: Float = 0.0F,          // Unit : %
    val skeletalMuscle: Float = 0.0F    // Unit : %
) : Parcelable