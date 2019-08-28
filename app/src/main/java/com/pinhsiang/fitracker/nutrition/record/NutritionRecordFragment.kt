package com.pinhsiang.fitracker.nutrition.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pinhsiang.fitracker.databinding.FragmentNutritionBinding
import com.pinhsiang.fitracker.databinding.FragmentNutritionRecordBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding

class NutritionRecordFragment : Fragment() {

    private lateinit var binding: FragmentNutritionRecordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentNutritionRecordBinding.inflate(inflater, container, false)
        return binding.root
    }
}