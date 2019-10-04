package com.pinhsiang.fitracker.workout.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.databinding.ItemWorkoutRecordSetsBinding
import com.pinhsiang.fitracker.util.Util.getColor

class RecordSetRVAdapter(val viewModel: WorkoutRecordViewModel) :
    ListAdapter<Sets, RecordSetRVAdapter.SetsViewHolder>(DiffCallback) {

    private var selectedPosition: Int = -1

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
        if (viewModel.reviseMode.value!!) {
            when (position) {
                selectedPosition -> {
                    holder.binding.titleSet.setTextColor(getColor(R.color.colorText))
                    holder.binding.numSet.setTextColor(getColor(R.color.colorText))
                    holder.binding.textWeight.setTextColor(getColor(R.color.colorText))
                    holder.binding.unitWeight.setTextColor(getColor(R.color.colorText))
                    holder.binding.textRep.setTextColor(getColor(R.color.colorText))
                    holder.binding.unitRep.setTextColor(getColor(R.color.colorText))
                }
                else -> {
                    holder.binding.titleSet.setTextColor(getColor(R.color.colorItem))
                    holder.binding.numSet.setTextColor(getColor(R.color.colorItem))
                    holder.binding.textWeight.setTextColor(getColor(R.color.colorItem))
                    holder.binding.unitWeight.setTextColor(getColor(R.color.colorItem))
                    holder.binding.textRep.setTextColor(getColor(R.color.colorItem))
                    holder.binding.unitRep.setTextColor(getColor(R.color.colorItem))
                }
            }

        } else if (!viewModel.reviseMode.value!!) {
            holder.binding.titleSet.setTextColor(getColor(R.color.colorText))
            holder.binding.numSet.setTextColor(getColor(R.color.colorText))
            holder.binding.textWeight.setTextColor(getColor(R.color.colorText))
            holder.binding.unitWeight.setTextColor(getColor(R.color.colorText))
            holder.binding.textRep.setTextColor(getColor(R.color.colorText))
            holder.binding.unitRep.setTextColor(getColor(R.color.colorText))
        }
        holder.itemView.setOnClickListener {
            if (selectedPosition != position) {
                selectedPosition = position
                viewModel.reviseItem(position)
            } else {
                viewModel.reviseItemDone()
                selectedPosition = -1
            }
            notifyDataSetChanged()
        }
        holder.bind(item)
    }
}