package com.pinhsiang.fitracker.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Nutrition(
    @get:Exclude var id: String = "",
    val time: Long = System.currentTimeMillis(),
    val title: String = "",
    var protein: Int = 0,       // Unit : g
    var carbohydrate: Int = 0,  // Unit : g
    var fat: Int =  0           // Unit : g
) : Parcelable