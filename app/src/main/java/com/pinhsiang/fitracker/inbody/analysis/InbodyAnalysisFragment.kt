package com.pinhsiang.fitracker.inbody.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.databinding.FragmentInbodyAnalysisBinding
import com.pinhsiang.fitracker.databinding.FragmentInbodyBinding
import com.pinhsiang.fitracker.databinding.FragmentNutritionBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding

class InbodyAnalysisFragment : Fragment() {

    private lateinit var binding: FragmentInbodyAnalysisBinding

    /**
     * Lazily initialize our [InbodyAnalysisViewModel].
     */
    private lateinit var viewModelFactory: InbodyAnalysisViewModelFactory
    private val viewModel: InbodyAnalysisViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(InbodyAnalysisViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInbodyAnalysisBinding.inflate(inflater, container, false)

        // Pass dataTime from workout fragment to detail fragment
        val application = requireNotNull(activity).application
        viewModelFactory = InbodyAnalysisViewModelFactory(application)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}