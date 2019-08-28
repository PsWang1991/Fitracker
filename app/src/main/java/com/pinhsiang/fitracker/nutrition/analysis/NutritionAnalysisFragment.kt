package com.pinhsiang.fitracker.nutrition.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pinhsiang.fitracker.databinding.FragmentNutritionAnalysisBinding
import com.pinhsiang.fitracker.databinding.FragmentNutritionBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding

class NutritionAnalysisFragment : Fragment() {

    private lateinit var binding: FragmentNutritionAnalysisBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentNutritionAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }
}