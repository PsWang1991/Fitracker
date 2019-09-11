package com.pinhsiang.fitracker.nutrition

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.util.Util.getString
import org.threeten.bp.LocalDate
import java.sql.Timestamp

const val TAG = "Fitracker"
const val USER_DOC_NAME = "U30OVkHZSDrYllYzjNlT"
const val MILLISECOND_PER_DAY = 86400000L
const val MILLISECOND_PER_WEEK = MILLISECOND_PER_DAY * 7
const val MILLISECOND_PER_MONTH = MILLISECOND_PER_DAY * 30
const val ZERO_HOUR = "00:00:00"

class NutritionViewModel(val app: Application) : AndroidViewModel(app) {

    private val db = FirebaseFirestore.getInstance()

    private val allNutritionData = mutableListOf<Nutrition>()

    private val _nutritionList = MutableLiveData<List<Nutrition>>()
    val nutritionList: LiveData<List<Nutrition>>
    get() = _nutritionList

    var selectedDate: LocalDate? = null
    val today = LocalDate.now()

    var calendarExpanding = true

    // Handle navigation to motion fragment.
    private val _navigationToRecord = MutableLiveData<Boolean>()
    val navigationToRecord: LiveData<Boolean>
        get() = _navigationToRecord

    val downloadComplete = MutableLiveData<Boolean>().apply {
        value = false
    }

    init {
        downloadWorkoutData()
    }

    fun addNewData() {
        _navigationToRecord.value = true
    }

    fun addNewDataDone() {
        _navigationToRecord.value = false
    }

    fun getNutritionDataByDate(date: LocalDate) {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        _nutritionList.value = allNutritionData.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }
//        Log.i(TAG, "allNutritionData = $allNutritionData")
//        Log.i(TAG, "nutritionList = ${nutritionList.value}")
    }

    fun hasNutritionData(date: LocalDate): Boolean {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        return allNutritionData.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }.isNotEmpty()
    }

    private fun downloadWorkoutData() {
        db.collection(getString(R.string.user_collection_path)).document(USER_DOC_NAME)
            .collection("nutrition")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nutrition = document.toObject(Nutrition::class.java)
                    nutrition.id = document.id
                    allNutritionData.add(nutrition)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }.addOnCompleteListener {
                Log.i(TAG, "allNutritionData = $allNutritionData")
                getNutritionDataByDate(LocalDate.now())
                downloadComplete.value = true
            }
    }

    fun refreshDataDone() {
        downloadComplete.value = false
    }
}