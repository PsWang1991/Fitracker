package com.pinhsiang.fitracker.inbody.record

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pinhsiang.fitracker.NavGraphDirections
import com.pinhsiang.fitracker.databinding.FragmentInbodyRecordBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.progress.DataUploadingFragment
import com.pinhsiang.fitracker.progress.UploadCompletelyFragment

class InBodyRecordFragment : Fragment() {

    private lateinit var binding: FragmentInbodyRecordBinding

    /**
     * Lazily initialize [InBodyRecordViewModel].
     */
    private val viewModel by viewModels<InBodyRecordViewModel> {
        getVmFactory(InBodyRecordFragmentArgs.fromBundle(arguments!!).inBody)
    }

    private lateinit var dataUploadingFragment: DialogFragment
    private lateinit var uploadCompletelyFragment: DialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInbodyRecordBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

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