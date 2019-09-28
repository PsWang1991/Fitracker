package com.pinhsiang.fitracker.inbody.record

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pinhsiang.fitracker.Double2StringConverter
import com.pinhsiang.fitracker.Float2StringConverter
import com.pinhsiang.fitracker.NavGraphDirections
import com.pinhsiang.fitracker.databinding.FragmentInbodyBinding
import com.pinhsiang.fitracker.databinding.FragmentInbodyRecordBinding
import com.pinhsiang.fitracker.databinding.FragmentNutritionBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding
import com.pinhsiang.fitracker.progress.DataUploadingFragment
import com.pinhsiang.fitracker.progress.UploadCompletelyFragment

class InbodyRecordFragment : Fragment() {

    private lateinit var binding: FragmentInbodyRecordBinding

    /**
     * Lazily initialize [InbodyRecordViewModel].
     */
    private lateinit var viewModelFactory: InbodyRecordViewModelFactory
    private val viewModel: InbodyRecordViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(InbodyRecordViewModel::class.java)
    }

    private lateinit var dataUploadingFragment: DialogFragment
    private lateinit var uploadCompletelyFragment: DialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInbodyRecordBinding.inflate(inflater, container, false)

        // Pass dataTime from workout fragment to detail fragment
        val application = requireNotNull(activity).application
        val inbody = InbodyRecordFragmentArgs.fromBundle(arguments!!).inbody
        viewModelFactory = InbodyRecordViewModelFactory(inbody, application)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

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

        uploadCompletelyFragment = UploadCompletelyFragment()
        viewModel.uploadDataDone.observe(this, Observer {
            it?.let {
                if (it) {
                    uploadCompletelyFragment.show(requireFragmentManager(), "upload_completely_fragment")
                    this.findNavController().navigate(NavGraphDirections.actionGlobalInbodyFragment())
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
}