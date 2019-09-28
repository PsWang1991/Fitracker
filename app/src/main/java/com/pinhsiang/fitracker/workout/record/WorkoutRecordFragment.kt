package com.pinhsiang.fitracker.workout.record

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.Int2StringConverter
import com.pinhsiang.fitracker.MainActivity
import com.pinhsiang.fitracker.databinding.FragmentWorkoutRecordBinding
import com.pinhsiang.fitracker.makeInVisible
import com.pinhsiang.fitracker.makeVisible
import com.pinhsiang.fitracker.progress.DataUploadingFragment

class WorkoutRecordFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutRecordBinding

    /**
     * Lazily initialize our [MotionViewModel].
     */
    private lateinit var viewModelFactory: WorkoutRecordViewModelFactory
    private val viewModel: WorkoutRecordViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(WorkoutRecordViewModel::class.java)
    }

    private lateinit var dataUploadingFragment: DialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutRecordBinding.inflate(inflater, container, false)

        // Pass dataTime from workout fragment to detail fragment
        val application = requireNotNull(activity).application
        val workout = WorkoutRecordFragmentArgs.fromBundle(arguments!!).workout
        viewModelFactory = WorkoutRecordViewModelFactory(workout, application)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.converter = Int2StringConverter

        // Setup sets RecyclerView adapter
        binding.rvSets.adapter = RecordSetRVAdapter(viewModel)

        // If set list change, notify RecyclerView to refresh.
        viewModel.setList.observe(this, Observer {
            (binding.rvSets.adapter as RecordSetRVAdapter).notifyDataSetChanged()
        })

        // value of repeats and weight can not be less than 0.
        viewModel.repeatsRecord.observe(this, Observer {
            if (it < 0) {
                viewModel.setRepeatsRecordTo0()
            }
        })
        viewModel.weightRecord.observe(this, Observer {
            if (it < 0) {
                viewModel.setWeightRecordTo0()
            }
        })

        viewModel.reviseMode.observe(this, Observer {
            if (it) {
                revisableOn()
            } else {
                revisableOff()
            }
        })

        viewModel.addToSetList.observe(this, Observer {
            if (it) {
                binding.rvSets.scrollToPosition(viewModel.endOfSetList())
                viewModel.setAddToSetListFalse()
            }
        })

        viewModel.restTimerStart.observe(this, Observer {
            when (it) {
                true -> (activity as MainActivity).window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                false -> (activity as MainActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        })

        dataUploadingFragment = DataUploadingFragment()
        viewModel.dataUploading.observe(this, Observer {
            it?.let {
                when(it) {
                    true -> {
                        dataUploadingFragment.show(requireFragmentManager(), "data_uploading_fragment")
                    }
                    false -> {
                        Handler().post(this::dismissDataUploadingFragment)
                    }
                }
            }
        })

        return binding.root
    }

    private fun dismissDataUploadingFragment() {
        dataUploadingFragment.dismiss()
    }

    private fun revisableOn() {
        binding.btnAddData.makeInVisible()
        binding.btnDeleteData.makeVisible()
        binding.btnReviseData.makeVisible()
    }

    private fun revisableOff() {
        binding.btnAddData.makeVisible()
        binding.btnDeleteData.makeInVisible()
        binding.btnReviseData.makeInVisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (viewModel.restTimerStart.value == true) {
            viewModel.restTimer.cancel()
        }
    }
}