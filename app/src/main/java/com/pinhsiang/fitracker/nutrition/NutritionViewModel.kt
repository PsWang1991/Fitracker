package com.pinhsiang.fitracker.nutrition

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.user.UserManager
import org.threeten.bp.LocalDate
import java.sql.Timestamp

class NutritionViewModel : ViewModel() {

    private val allNutritionData = mutableListOf<Nutrition>()

    private val _nutritionList = MutableLiveData<List<Nutrition>>()
    val nutritionList: LiveData<List<Nutrition>>
        get() = _nutritionList

    private val _hasData = MutableLiveData<Boolean>().apply {
        value = false
    }
    val hasData: LiveData<Boolean>
        get() = _hasData

    var selectedDate: LocalDate? = null
    val today: LocalDate = LocalDate.now()

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

    fun refreshNutritionListByDate(date: LocalDate) {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        _nutritionList.value = allNutritionData.filter {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }
        _hasData.value = _nutritionList.value!!.isNotEmpty()
    }

    fun hasNutritionData(date: LocalDate): Boolean {
        val dateToStartTimestamp = Timestamp.valueOf("$date $ZERO_HOUR").time
        return allNutritionData.any {
            it.time in dateToStartTimestamp until dateToStartTimestamp + MILLISECOND_PER_DAY
        }
    }

    private fun downloadWorkoutData() {

        FirebaseFirestore.getInstance()
            .collection(USER)
            .document(UserManager.userDocId!!)
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
            }
            .addOnCompleteListener {

                Log.i(TAG, "allNutritionData = $allNutritionData")
                refreshNutritionListByDate(LocalDate.now())
                downloadComplete.value = true
            }
    }

    fun refreshDataDone() {
        downloadComplete.value = false
    }
}