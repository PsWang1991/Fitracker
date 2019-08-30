package com.pinhsiang.fitracker

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.workout.WorkoutRVAdapter
import com.pinhsiang.fitracker.workout.WorkoutSetsRVAdapter

// Adapter for workout fragment's workout recycle view
@BindingAdapter("workout")
fun bindRecyclerViewWithWorkout(recyclerView: RecyclerView, data: List<Workout>?) {
    val adapter = recyclerView.adapter as WorkoutRVAdapter
    adapter.submitList(data)
}

// Adapter for item_workout's sets recycle view
@BindingAdapter("workoutSets")
fun bindRecyclerViewWithWorkoutSets(recyclerView: RecyclerView, data: List<Sets>?) {
    val adapter = recyclerView.adapter as WorkoutSetsRVAdapter
    adapter.submitList(data)
}