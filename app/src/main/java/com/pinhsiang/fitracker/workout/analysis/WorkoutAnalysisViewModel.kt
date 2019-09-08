package com.pinhsiang.fitracker.workout.analysis

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout


const val TAG = "Fitracker"
const val USER_DOC_NAME = "U30OVkHZSDrYllYzjNlT"

class WorkoutAnalysisViewModel(app: Application) : AndroidViewModel(app) {

    val db = FirebaseFirestore.getInstance()

    val userDocId = MutableLiveData<String>()

    private val _selectedExercise = MutableLiveData<String>()
    val selectedExercise: LiveData<String>
        get() = _selectedExercise

    fun setWorkoutMotion(motion: String) {
        _selectedExercise.value = motion
    }

    private val allWorkoutData = mutableListOf<Workout>()

    init {
        getWorkoutData()
    }

//    private fun getUserDocId() {
//        db.collection("user")
//            .whereEqualTo("email", "pinhsiang@pmail.com")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    userDocId.value = document.id
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }
//    }

    private fun getWorkoutData() {
        db.collection("user").document(USER_DOC_NAME)
            .collection("workout")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val workout = document.toObject(Workout::class.java)
                    allWorkoutData.add(workout)
//                    Log.i(TAG, "workout = $workout")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }.addOnCompleteListener {
                Log.w(TAG, "allWorkoutData = $allWorkoutData")
                Log.w(TAG, "allWorkoutData size = ${allWorkoutData.size}")
            }
    }

//    private fun addWorkoutData() {
//        val set1 = Sets(liftWeight = 100, repeats = 3)
//        val set2 = Sets(liftWeight = 100, repeats = 2)
//        val workout = Workout(motion = "Deadlift", sets = listOf(set1, set2))
//        db.collection("user").document(USER_DOC_NAME)
//            .collection("workout").add(workout)
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }
//            .addOnCompleteListener {
//                getWorkoutData()
//            }
//    }

//    private fun addNutritionData() {
//        val nutrition = Nutrition(
//            id = "sasadog",
//            time = System.currentTimeMillis(),
//            title = "Rice ball",
//            protein = 5,
//            carbohydrate = 100,
//            fat = 3
//        )
//        db.collection("user").document(USER_DOC_NAME)
//            .collection("nutrition").add(nutrition)
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }
//            .addOnCompleteListener {
//                getNutritionData()
//            }
//    }

//    private fun getNutritionData() {
//        db.collection("user").document(USER_DOC_NAME)
//            .collection("nutrition")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val nutrition = document.toObject(Nutrition::class.java)
//                    nutrition.id = document.id
//                    Log.i(TAG, "nutrition = $nutrition")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }
//    }

}

