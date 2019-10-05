package com.pinhsiang.fitracker.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.databinding.FragmentUploadingBinding

class DataUploadingFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentUploadingBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawableResource(R.drawable.btn_text_border)

        return binding.root
    }
}