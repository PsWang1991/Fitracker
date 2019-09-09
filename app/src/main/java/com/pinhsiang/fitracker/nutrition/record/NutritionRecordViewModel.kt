package com.pinhsiang.fitracker.nutrition.record

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.timestampToDate
import com.pinhsiang.fitracker.timestampToString

const val TAG = "Fitracker"
const val USER_DOC_NAME = "U30OVkHZSDrYllYzjNlT"


class NutritionRecordViewModel(private val selectedNutrition: Nutrition, val app: Application) : AndroidViewModel(app) {

    private val db = FirebaseFirestore.getInstance()

    val selectedDate = selectedNutrition.time.timestampToDate()

    private var nutritionToUpload = selectedNutrition

    val titleRecord = MutableLiveData<String>().apply {
        value = selectedNutrition.title
    }

    val proteinRecord = MutableLiveData<Int>().apply {
        value = selectedNutrition.protein
    }

    val carbohydrateRecord = MutableLiveData<Int>().apply {
        value = selectedNutrition.carbohydrate
    }

    val fatRecord = MutableLiveData<Int>().apply {
        value = selectedNutrition.fat
    }

    init {
        Log.i(TAG, "**********   NutritionRecordViewModel   *********")
        Log.i(TAG, "Selected Nutrition = $selectedNutrition")
        Log.i(TAG, "Title = ${selectedNutrition.title}")
        Log.i(TAG, "Date = ${selectedNutrition.time.timestampToString()}")
        Log.i(TAG, "**********   NutritionRecordViewModel   *********")
    }

    fun saveData() {
        Log.i(TAG, "titleRecord = ${titleRecord.value}")
        Log.i(TAG, "proteinRecord = ${proteinRecord.value}")
        Log.i(TAG, "carbohydrateRecord = ${carbohydrateRecord.value}")
        if (titleRecord.value != "") {
            nutritionToUpload = Nutrition(
                time = selectedNutrition.time,
                title = titleRecord.value!!,
                protein = proteinRecord.value!!,
                carbohydrate = carbohydrateRecord.value!!,
                fat = fatRecord.value!!
            )
            Log.i(TAG, "nutritionToUpload = $nutritionToUpload")
            db.collection("user").document(USER_DOC_NAME)
                .collection("nutrition").add(nutritionToUpload)
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
                .addOnCompleteListener {
                    Toast.makeText(app, "Data saving completed.", Toast.LENGTH_SHORT).show()
                }

        } else {
            Toast.makeText(app, "Title can not be blank.", Toast.LENGTH_SHORT).show()
        }
    }

    fun proteinPlus1() {
        proteinRecord.value = proteinRecord.value?.plus(1)
    }

    fun proteinMinus1() {
        proteinRecord.value = proteinRecord.value?.minus(1)
    }

    fun carbohydratePlus1() {
        carbohydrateRecord.value = carbohydrateRecord.value?.plus(1)
    }

    fun carbohydrateMinus1() {
        carbohydrateRecord.value = carbohydrateRecord.value?.minus(1)
    }

    fun fatPlus1() {
        fatRecord.value = fatRecord.value?.plus(1)
    }

    fun fatMinus1() {
        fatRecord.value = fatRecord.value?.minus(1)
    }

    fun setProteinRecordTo0() {
        proteinRecord.value = 0
    }

    fun setCarbohydrateRecordTo0() {
        carbohydrateRecord.value = 0
    }

    fun setFatRecordRecordTo0() {
        fatRecord.value = 0
    }
}