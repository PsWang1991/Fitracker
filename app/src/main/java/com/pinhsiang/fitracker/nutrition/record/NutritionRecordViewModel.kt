package com.pinhsiang.fitracker.nutrition.record

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.FitrackerApplication
import com.pinhsiang.fitracker.NUTRITION
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.USER
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.ext.timestampToDate
import com.pinhsiang.fitracker.ext.timestampToString
import com.pinhsiang.fitracker.user.UserManager

class NutritionRecordViewModel(private val selectedNutrition: Nutrition) : ViewModel() {

    val selectedDate = selectedNutrition.time.timestampToDate()

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

    private val _dataUploading = MutableLiveData<Boolean>()
    val dataUploading: LiveData<Boolean>
        get() = _dataUploading

    private val _uploadDataDone = MutableLiveData<Boolean>()
    val uploadDataDone: LiveData<Boolean>
        get() = _uploadDataDone

    init {
        Log.i(TAG, "**********   NutritionRecordViewModel   *********")
        Log.i(TAG, "Selected Nutrition = $selectedNutrition")
        Log.i(TAG, "Title = ${selectedNutrition.title}")
        Log.i(TAG, "Date = ${selectedNutrition.time.timestampToString()}")
        Log.i(TAG, "**********   NutritionRecordViewModel   *********")
    }

    fun uploadData() {

        if (titleRecord.value == "") {

            Toast.makeText(
                FitrackerApplication.appContext,
                "Title can not be blank.",
                Toast.LENGTH_SHORT
            ).show()

        } else if (proteinRecord.value == 0 && carbohydrateRecord.value == 0 && fatRecord.value == 0) {

            Toast.makeText(
                FitrackerApplication.appContext,
                "Nutrients are all zero",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            val nutritionToUpload = Nutrition(
                time = selectedNutrition.time,
                title = titleRecord.value!!,
                protein = proteinRecord.value!!,
                carbohydrate = carbohydrateRecord.value!!,
                fat = fatRecord.value!!
            )

            _dataUploading.value = true

            FirebaseFirestore.getInstance()
                .collection(USER)
                .document(UserManager.userDocId!!)
                .collection(NUTRITION)
                .add(nutritionToUpload)
                .addOnFailureListener { exception ->

                    _dataUploading.value = false
                    Toast.makeText(
                        FitrackerApplication.appContext,
                        "Uploading failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w(TAG, "Error getting documents.", exception)
                }
                .addOnCompleteListener {

                    _dataUploading.value = false
                    _uploadDataDone.value = true
                    Toast.makeText(
                        FitrackerApplication.appContext,
                        "Data saving completed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }

    fun uploadCompletely() {
        _uploadDataDone.value = false
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