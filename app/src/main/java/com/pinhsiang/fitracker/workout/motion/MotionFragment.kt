package com.pinhsiang.fitracker.workout.motion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutMotionBinding

class MotionFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutMotionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutMotionBinding.inflate(inflater, container, false)
        return binding.root
    }
}