package com.pinhsiang.fitracker.workout.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.databinding.FragmentWorkoutRecordBinding

class WorkoutRecordFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutRecordBinding

    /**
     * Lazily initialize our [MotionViewModel].
     */
    private lateinit var viewModelFactory: WorkoutRecordViewModelFactory
    private val viewModel: WorkoutRecordViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(WorkoutRecordViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutRecordBinding.inflate(inflater, container, false)

        // Pass dataTime from workout fragment to detail fragment
        val application = requireNotNull(activity).application
        val workout = WorkoutRecordFragmentArgs.fromBundle(arguments!!).workout
        viewModelFactory = WorkoutRecordViewModelFactory(workout, application)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}