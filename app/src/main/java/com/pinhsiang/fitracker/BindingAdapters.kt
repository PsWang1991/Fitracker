package com.pinhsiang.fitracker

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.data.*
import com.pinhsiang.fitracker.nutrition.NutritionRVAdapter
import com.pinhsiang.fitracker.recommended.YoutubeRecyclerAdapter
import com.pinhsiang.fitracker.timer.TimerPatternRVAdapter
import com.pinhsiang.fitracker.workout.WorkoutRVAdapter
import com.pinhsiang.fitracker.workout.WorkoutSetsRVAdapter
import com.pinhsiang.fitracker.workout.motion.MotionRVAdapter
import com.pinhsiang.fitracker.workout.record.RecordSetRVAdapter

// Adapter for workout fragment's workout RecyclerView
@BindingAdapter("workout")
fun bindRecyclerViewWithWorkout(recyclerView: RecyclerView, data: List<Workout>?) {
    val adapter = recyclerView.adapter as WorkoutRVAdapter
    adapter.submitList(data)
}

// Adapter for item_workout's sets RecyclerView
@BindingAdapter("workoutSets")
fun bindRecyclerViewWithWorkoutSets(recyclerView: RecyclerView, data: List<Sets>?) {
    when (val adapter = recyclerView.adapter) {
        is WorkoutSetsRVAdapter -> adapter.submitList(data)
        is RecordSetRVAdapter -> adapter.submitList(data)
    }
}

// Adapter for item_workout's sets RecyclerView
@BindingAdapter("motionList")
fun bindRecyclerViewWithMotion(recyclerView: RecyclerView, data: List<Motion>?) {
    val adapter = recyclerView.adapter as MotionRVAdapter
    adapter.submitList(data)
}

// Adapter for nutrition fragment's nutrition RecyclerView
@BindingAdapter("nutritionList")
fun bindRecyclerViewWithNutrition(recyclerView: RecyclerView, data: List<Nutrition>?) {
    val adapter = recyclerView.adapter as NutritionRVAdapter
    adapter.submitNutritionList(data)
}

// Adapter for timer fragment's timer pattern RecyclerView
@BindingAdapter("patternList")
fun bindRecyclerViewWithTimerPattern(recyclerView: RecyclerView, data: List<TimerPattern>?) {
    val adapter = recyclerView.adapter as TimerPatternRVAdapter
    adapter.submitList(data)
}

// Adapter for recommended activity's  RecyclerView
@BindingAdapter("youtubeList")
fun bindRecyclerViewWithYoutubeVideo(recyclerView: RecyclerView, data: List<YoutubeVideo>?) {
    val adapter = recyclerView.adapter as YoutubeRecyclerAdapter
    adapter.submitList(data)
}