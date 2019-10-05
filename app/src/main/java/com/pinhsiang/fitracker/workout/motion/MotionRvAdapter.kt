package com.pinhsiang.fitracker.workout.motion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.data.Motion
import com.pinhsiang.fitracker.databinding.ItemMotionBinding

class MotionRvAdapter(val viewModel: MotionViewModel) :
    ListAdapter<Motion, MotionRvAdapter.MotionViewHolder>(DiffCallback) {

    class MotionViewHolder(var binding: ItemMotionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(motion: Motion) {
            binding.motion = motion
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Motion]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Motion>() {

        override fun areItemsTheSame(oldItem: Motion, newItem: Motion): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Motion, newItem: Motion): Boolean {
            return oldItem.motion == newItem.motion
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotionViewHolder {

        return MotionViewHolder(
            ItemMotionBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: MotionViewHolder, position: Int) {

        val item = getItem(position)

        holder.binding.imgMotion.background
        holder.itemView.setOnClickListener {
            viewModel.selectMotion(item.motion)
        }
        holder.bind(item)
    }

}