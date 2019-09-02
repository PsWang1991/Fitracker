package com.pinhsiang.fitracker

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.data.Motion
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.workout.WorkoutRVAdapter
import com.pinhsiang.fitracker.workout.WorkoutSetsRVAdapter
import com.pinhsiang.fitracker.workout.motion.MotionRVAdapter

// Adapter for workout fragment's workout RecyclerView
@BindingAdapter("workout")
fun bindRecyclerViewWithWorkout(recyclerView: RecyclerView, data: List<Workout>?) {
    val adapter = recyclerView.adapter as WorkoutRVAdapter
    adapter.submitList(data)
}

// Adapter for item_workout's sets RecyclerView
@BindingAdapter("workoutSets")
fun bindRecyclerViewWithWorkoutSets(recyclerView: RecyclerView, data: List<Sets>?) {
    val adapter = recyclerView.adapter as WorkoutSetsRVAdapter
    adapter.submitList(data)
}

// Adapter for item_workout's sets RecyclerView
@BindingAdapter("motionList")
fun bindRecyclerViewWithMotion(recyclerView: RecyclerView, data: List<Motion>?) {
    val adapter = recyclerView.adapter as MotionRVAdapter
    adapter.submitList(data)
}