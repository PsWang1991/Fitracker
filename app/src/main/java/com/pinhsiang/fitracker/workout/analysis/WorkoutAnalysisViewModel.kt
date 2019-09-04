package com.pinhsiang.fitracker.workout.analysis

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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


    init {
//        addWorkoutData()
//        getUserDocId()
//        addNutritionData()
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
//            .addOnCompleteListener {
//
//            }
//    }

    fun getWorkoutData() {
        db.collection("user").document(userDocId.value!!)
            .collection("workout")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
//                    val sets = arrayListOf<Sets>()
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val time = document.data.getValue("time")
                    val motion = document.data.getValue("motion")
                    Log.d(TAG, "time => $time")
                    Log.d(TAG, "motion => $motion")
                    val sets: MutableList<HashMap<String, Int>> = document.data.getValue("sets") as MutableList<HashMap<String, Int>>
                    sets.forEachIndexed  { index, set ->
                        Log.d(TAG, "Set ${index+1} liftWeight => ${set.getValue("liftWeight")}")
                        Log.d(TAG, "Set ${index+1} repeats => ${set.getValue("repeats")}")

                    }
//                    Log.d(TAG, "sets => $sets")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

//    private fun addWorkoutData() {
//        val set1 = Sets(liftWeight = 50, repeats = 10)
//        val set2 = Sets(liftWeight = 80, repeats = 6)
//        val workout = Workout(motion = "Squat", sets = listOf(set1, set2))
//        db.collection("user").document("U30OVkHZSDrYllYzjNlT")
//            .collection("workout").add(workout)
//
//    }

    private fun addNutritionData() {
        val nutrition = Nutrition(
            id = "123",
            time = System.currentTimeMillis(),
            title = "Lunch",
            protein = 45,
            carbohydrate = 60,
            fat = 15
        )
        db.collection("user").document(USER_DOC_NAME)
            .collection("nutrition").add(nutrition)
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

}

