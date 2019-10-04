package com.pinhsiang.fitracker

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pinhsiang.fitracker.data.*
import com.pinhsiang.fitracker.nutrition.NutritionRvAdapter
import com.pinhsiang.fitracker.recommended.YoutubeRvAdapter
import com.pinhsiang.fitracker.timer.TimerPatternRvAdapter
import com.pinhsiang.fitracker.workout.WorkoutRvAdapter
import com.pinhsiang.fitracker.workout.WorkoutSetsRvAdapter
import com.pinhsiang.fitracker.workout.motion.MotionRvAdapter
import com.pinhsiang.fitracker.workout.record.RecordSetRvAdapter

// Adapter for workout fragment's workout RecyclerView
@BindingAdapter("workout")
fun bindRecyclerViewWithWorkout(recyclerView: RecyclerView, data: List<Workout>?) {
    val adapter = recyclerView.adapter as WorkoutRvAdapter
    adapter.submitList(data)
}

// Adapter for item_workout's sets RecyclerView
@BindingAdapter("workoutSets")
fun bindRecyclerViewWithWorkoutSets(recyclerView: RecyclerView, data: List<Sets>?) {
    when (val adapter = recyclerView.adapter) {
        is WorkoutSetsRvAdapter -> adapter.submitList(data)
        is RecordSetRvAdapter -> adapter.submitList(data)
    }
}

// Adapter for item_workout's sets RecyclerView
@BindingAdapter("motionList")
fun bindRecyclerViewWithMotion(recyclerView: RecyclerView, data: List<Motion>?) {
    val adapter = recyclerView.adapter as MotionRvAdapter
    adapter.submitList(data)
}

// Adapter for nutrition fragment's nutrition RecyclerView
@BindingAdapter("nutritionList")
fun bindRecyclerViewWithNutrition(recyclerView: RecyclerView, data: List<Nutrition>?) {
    val adapter = recyclerView.adapter as NutritionRvAdapter
    adapter.submitNutritionList(data)
}

// Adapter for timer fragment's timer pattern RecyclerView
@BindingAdapter("patternList")
fun bindRecyclerViewWithTimerPattern(recyclerView: RecyclerView, data: List<TimerPattern>?) {
    val adapter = recyclerView.adapter as TimerPatternRvAdapter
    adapter.submitList(data)
}

// Adapter for recommended activity's  RecyclerView
@BindingAdapter("youtubeList")
fun bindRecyclerViewWithYoutubeVideo(recyclerView: RecyclerView, data: List<YoutubeVideo>?) {
    val adapter = recyclerView.adapter as YoutubeRvAdapter
    adapter.submitList(data)
}

// Converting imgUrl to a URI with the https scheme and transfer to circular image.
@BindingAdapter("circularAvatar")
fun bindCircularAvatar(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions
                    .circleCropTransform()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_avatar_36dp_3)
            )
            .into(imgView)
    }
}