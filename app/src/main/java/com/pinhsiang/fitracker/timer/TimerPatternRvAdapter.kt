package com.pinhsiang.fitracker.timer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.TimerPattern
import com.pinhsiang.fitracker.databinding.ItemTimerPatternBinding
import com.pinhsiang.fitracker.util.Util.getColor

class TimerPatternRvAdapter(val viewModel: TimerViewModel) :
    ListAdapter<TimerPattern, TimerPatternRvAdapter.PatternViewHolder>(DiffCallback) {

    private var selectedPosition: Int = -1

    class PatternViewHolder(var binding: ItemTimerPatternBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pattern: TimerPattern) {
            binding.pattern = pattern
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [TimerPattern] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<TimerPattern>() {
        override fun areItemsTheSame(oldItem: TimerPattern, newItem: TimerPattern): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TimerPattern, newItem: TimerPattern): Boolean {
            return (oldItem.exerciseTime == newItem.exerciseTime &&
                    oldItem.restTime == newItem.restTime &&
                    oldItem.repeat == newItem.repeat)
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatternViewHolder {
        return PatternViewHolder(
            ItemTimerPatternBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: PatternViewHolder, position: Int) {
        val item = getItem(position)
        if (viewModel.reviseMode.value!!) {
            when (position) {
                selectedPosition -> {
                    holder.binding.exerciseTime.setTextColor(getColor(R.color.colorText))
                    holder.binding.restTime.setTextColor(getColor(R.color.colorText))
                    holder.binding.textPatternRepeats.setTextColor(getColor(R.color.colorText))
                }
                else -> {
                    holder.binding.exerciseTime.setTextColor(getColor(R.color.colorItem))
                    holder.binding.restTime.setTextColor(getColor(R.color.colorItem))
                    holder.binding.textPatternRepeats.setTextColor(getColor(R.color.colorItem))
                }
            }

        } else if (!viewModel.reviseMode.value!!) {
            holder.binding.exerciseTime.setTextColor(getColor(R.color.colorText))
            holder.binding.restTime.setTextColor(getColor(R.color.colorText))
            holder.binding.textPatternRepeats.setTextColor(getColor(R.color.colorText))
        }
        holder.itemView.setOnClickListener {
            if (selectedPosition != position) {
                selectedPosition = position
                viewModel.reviseModeOn(position)
            } else {
                viewModel.reviseModeOff()
                selectedPosition = -1
            }
            notifyDataSetChanged()
        }
        holder.bind(item)
    }
}