package com.pinhsiang.fitracker.workout.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutRecordBinding

class WorkoutRecordFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutRecordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutRecordBinding.inflate(inflater, container, false)
        return binding.root
    }
}