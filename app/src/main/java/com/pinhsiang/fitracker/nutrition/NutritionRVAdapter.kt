package com.pinhsiang.fitracker.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.databinding.ItemNutritionBinding
import com.pinhsiang.fitracker.util.Util.getString

class NutritionRVAdapter : ListAdapter<Nutrition, NutritionRVAdapter.NutritionViewHolder>(DiffCallback) {

    class NutritionViewHolder(var binding: ItemNutritionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nutrition: Nutrition) {
            binding.nutrition = nutrition
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Workout]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Nutrition>() {
        override fun areItemsTheSame(oldItem: Nutrition, newItem: Nutrition): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Nutrition, newItem: Nutrition): Boolean {
            return (oldItem.title == newItem.title
                    && oldItem.time == newItem.time
                    && oldItem.protein == newItem.protein
                    )
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionViewHolder {
        return NutritionViewHolder(
            ItemNutritionBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: NutritionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    // New function to submit list and also adds total item on top.
    fun submitNutritionList(list: List<Nutrition>?) {
        val newList = mutableListOf<Nutrition?>()

        if (list != null) {
            if (list.isNotEmpty()) {
                var totalProtein = 0
                var totalCarbohydrate = 0
                var totalFat = 0
                for (nutrition in list) {
                    totalProtein += nutrition.protein
                    totalCarbohydrate += nutrition.carbohydrate
                    totalFat += nutrition.fat
                }
                val totalNutrition = Nutrition(
                    time = list[0].time,
                    title = getString(R.string.total),
                    protein = totalProtein,
                    carbohydrate = totalCarbohydrate,
                    fat = totalFat
                )
                newList.add(totalNutrition)
                list.let { newList.addAll(it) }
            }
        }
        submitList(newList)
    }

}