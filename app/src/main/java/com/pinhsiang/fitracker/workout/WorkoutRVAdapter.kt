package com.pinhsiang.fitracker.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.FitrackerApplication
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.databinding.ItemWorkoutBinding

class WorkoutRVAdapter : ListAdapter<Workout, WorkoutRVAdapter.WorkoutViewHolder>(DiffCallback) {

    class WorkoutViewHolder(var binding: ItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(workout: Workout) {
            binding.workout = workout
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Workout]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return (oldItem.motion == newItem.motion
                    && oldItem.time == newItem.time
                    && oldItem.sets == newItem.sets
                    )
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            ItemWorkoutBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val item = getItem(position)
        val manager = LinearLayoutManager(FitrackerApplication.appContext)
        holder.binding.rvWorkoutSets.layoutManager = manager
        holder.binding.rvWorkoutSets.adapter = WorkoutSetsRVAdapter()
        holder.bind(item)
    }


}