package com.pinhsiang.fitracker.workout.motion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pinhsiang.fitracker.data.Sets
import com.pinhsiang.fitracker.data.Workout
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutMotionBinding
import com.pinhsiang.fitracker.timestampToString

class MotionFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutMotionBinding

    /**
     * Lazily initialize our [MotionViewModel].
     */
    private lateinit var viewModelFactory: MotionViewModelFactory
    private val viewModel: MotionViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MotionViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutMotionBinding.inflate(inflater, container, false)

        // Pass dataTime from workout fragment to detail fragment
        val application = requireNotNull(activity).application
        val dataTime = MotionFragmentArgs.fromBundle(arguments!!).time
        viewModelFactory = MotionViewModelFactory(dataTime, application)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Setup motion RecyclerView adapter
        binding.rvMotion.adapter = MotionRVAdapter(viewModel)

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

        binding.textView2.text = viewModel.dataTime.timestampToString()

        return binding.root
    }
}