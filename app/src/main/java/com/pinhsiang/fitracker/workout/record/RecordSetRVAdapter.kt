package com.pinhsiang.fitracker.workout.record

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.databinding.ItemWorkoutRecordSetsBinding

class RecordSetRVAdapter : ListAdapter<Sets, RecordSetRVAdapter.SetsViewHolder>(DiffCallback) {

    class SetsViewHolder(var binding: ItemWorkoutRecordSetsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sets: Sets) {
            binding.set = sets
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Sets] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Sets>() {
        override fun areItemsTheSame(oldItem: Sets, newItem: Sets): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Sets, newItem: Sets): Boolean {
            return (oldItem.liftWeight == newItem.liftWeight && oldItem.repeats == newItem.repeats)
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetsViewHolder {
        return SetsViewHolder(
            ItemWorkoutRecordSetsBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: SetsViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.numSet.text = (position + 1).toString()
        holder.itemView.setOnClickListener {
            Log.i(TAG, "position = $position")
        }
        holder.bind(item)
    }
}