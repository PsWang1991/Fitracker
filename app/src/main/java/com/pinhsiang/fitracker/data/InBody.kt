package com.pinhsiang.fitracker.data

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InBody(

    @get:Exclude var id: String = "",
    val time: Long = System.currentTimeMillis(),
    val bodyWeight: Float = 0f,       // Unit : Kg
    val bodyFat: Float = 0f,          // Unit : %
    val skeletalMuscle: Float = 0f    // Unit : %

) : Parcelable