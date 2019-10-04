package com.pinhsiang.fitracker.workout.motion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pinhsiang.fitracker.TAG
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.databinding.FragmentWorkoutMotionBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.ext.timestampToDate

class MotionFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutMotionBinding

    /**
     * Lazily initialize our [MotionViewModel].
     */
    private val viewModel by viewModels<MotionViewModel> {
        getVmFactory(MotionFragmentArgs.fromBundle(arguments!!).time)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutMotionBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Setup motion RecyclerView adapter
        binding.rvMotion.adapter = MotionRvAdapter(viewModel)

        viewModel.selectedMotion.observe(this, Observer {
            it?.let { motion ->
                Log.i(TAG, "Motion : $motion")
                val addedWorkout = Workout(
                    time = viewModel.dataTime,
                    motion = motion
                )
                this.findNavController()
                    .navigate(MotionFragmentDirections.ActionMotionFragmentToWorkoutRecordFragment(addedWorkout))
                viewModel.setMotionDone()
            }
        })

        binding.textDateMotion.text = viewModel.dataTime.timestampToDate()

        return binding.root
    }
}