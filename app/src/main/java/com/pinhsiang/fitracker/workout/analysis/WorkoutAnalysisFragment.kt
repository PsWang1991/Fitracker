package com.pinhsiang.fitracker.workout.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.databinding.FragmentWorkoutAnalysisBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding

class WorkoutAnalysisFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutAnalysisBinding

    /**
     * Lazily initialize our [MotionViewModel].
     */
    private lateinit var viewModelFactory: WorkoutAnalysisViewModelFactory
    private val viewModel: WorkoutAnalysisViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(WorkoutAnalysisViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutAnalysisBinding.inflate(inflater, container, false)

        // Pass dataTime from workout fragment to detail fragment
        val application = requireNotNull(activity).application
        viewModelFactory = WorkoutAnalysisViewModelFactory(application)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.userDocId.observe(this, Observer {
            viewModel.getWorkoutData()
        })

        return binding.root
    }
}