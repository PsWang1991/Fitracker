package com.pinhsiang.fitracker.workout.record

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pinhsiang.fitracker.Int2StringConverter
import com.pinhsiang.fitracker.MainActivity
import com.pinhsiang.fitracker.NavGraphDirections
import com.pinhsiang.fitracker.databinding.FragmentWorkoutRecordBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.ext.makeInVisible
import com.pinhsiang.fitracker.ext.makeVisible
import com.pinhsiang.fitracker.progress.DataUploadingFragment
import com.pinhsiang.fitracker.progress.UploadCompletelyFragment

class WorkoutRecordFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutRecordBinding

    /**
     * Lazily initialize our [WorkoutRecordViewModel].
     */
    private val viewModel by viewModels<WorkoutRecordViewModel> {
        getVmFactory(WorkoutRecordFragmentArgs.fromBundle(arguments!!).workout)
    }

    private lateinit var dataUploadingFragment: DialogFragment
    private lateinit var uploadCompletelyFragment: DialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutRecordBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.converter = Int2StringConverter

        // Setup sets RecyclerView adapter
        binding.rvSets.adapter = RecordSetRvAdapter(viewModel)

        // If set list change, notify RecyclerView to refresh.
        viewModel.setList.observe(this, Observer {
            (binding.rvSets.adapter as RecordSetRvAdapter).notifyDataSetChanged()
        })

        /**
         *  Values can not be less than 0.
         */
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
            when (it) {
                true -> reviseModeOn()
                false -> reviseModeOff()
            }
        })

        viewModel.addNewItem.observe(this, Observer {
            if (it) {
                binding.rvSets.scrollToPosition(viewModel.endOfSetList())
                viewModel.addNewItemDone()
            }
        })

        viewModel.restTimerStart.observe(this, Observer {
            when (it) {
                true -> (activity as MainActivity).window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                false -> (activity as MainActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        })

        dataUploadingFragment = DataUploadingFragment()

        viewModel.dataUploading.observe(this, Observer { dataUploading ->
            dataUploading?.let {
                when (it) {
                    true -> {
                        dataUploadingFragment.show(requireFragmentManager(), "data_uploading_fragment")
                    }
                    false -> {
                        Handler().post(this::dismissDataUploadingFragment)
                    }
                }
            }
        })

        uploadCompletelyFragment = UploadCompletelyFragment()

        viewModel.uploadDataDone.observe(this, Observer { uploadDataDone ->
            uploadDataDone?.let {
                if (it) {
                    uploadCompletelyFragment.show(requireFragmentManager(), "upload_completely_fragment")
                    this.findNavController().navigate(NavGraphDirections.actionGlobalWorkoutFragment())
                    Handler().postDelayed(this::dismissUploadCompletelyFragment, 2000)
                    viewModel.uploadCompletely()
                }
            }
        })

        return binding.root
    }

    private fun dismissDataUploadingFragment() {
        dataUploadingFragment.dismiss()
    }

    private fun dismissUploadCompletelyFragment() {
        uploadCompletelyFragment.dismiss()
    }

    private fun reviseModeOn() {
        binding.btnAddData.makeInVisible()
        binding.btnDeleteData.makeVisible()
        binding.btnReviseData.makeVisible()
    }

    private fun reviseModeOff() {
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