package com.pinhsiang.fitracker.tdee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.databinding.FragmentTdeeBinding
import com.pinhsiang.fitracker.ext.getVmFactory

const val TAG = "Fitracker"

class TDEEFragment : Fragment() {

    private lateinit var binding: FragmentTdeeBinding

    /**
     * Lazily initialize [TDEEViewModel].
     */
    private val viewModel by viewModels<TDEEViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTdeeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Bind ViewModel
        binding.viewModel = viewModel

        binding.radioGroupGender.setOnCheckedChangeListener { _, radioBtnId ->
            when (radioBtnId) {
                R.id.radio_male -> viewModel.setGenderMale()
                R.id.radio_female -> viewModel.setGenderFemale()
            }
        }
        return binding.root
    }
}