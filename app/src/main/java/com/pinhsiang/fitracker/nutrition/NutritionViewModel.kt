package com.pinhsiang.fitracker.nutrition

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.MILLISECOND_PER_DAY
import com.pinhsiang.fitracker.NUTRITION
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.USER
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.user.UserManager
import org.threeten.bp.LocalDate
import java.sql.Timestamp

const val ZERO_HOUR = "00:00:00"

class NutritionViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val allNutritionData = mutableListOf<Nutrition>()

    private val _nutritionList = MutableLiveData<List<Nutrition>>()
    val nutritionList: LiveData<List<Nutrition>>
    get() = _nutritionList

    val dataStatus = MutableLiveData<Boolean>().apply {
        value = false
    }

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

    fun setDataStatusByDate(date: LocalDate) {
        dataStatus.value = hasNutritionData(date)
    }

    private fun downloadWorkoutData() {
        db.collection(USER).document(UserManager.userDocId!!)
            .collection(NUTRITION)
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
                setDataStatusByDate(LocalDate.now())
                downloadComplete.value = true
            }
    }

    fun refreshDataDone() {
        downloadComplete.value = false
    }
}