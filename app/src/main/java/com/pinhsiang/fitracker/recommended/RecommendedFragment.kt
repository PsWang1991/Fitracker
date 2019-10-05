package com.pinhsiang.fitracker.recommended

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pinhsiang.fitracker.databinding.FragmentRecommendedBinding
import com.pinhsiang.fitracker.ext.getVmFactory

class RecommendedFragment : Fragment() {

    lateinit var binding: FragmentRecommendedBinding

    /**
     * Lazily initialize our [RecommendedViewModel].
     */
    private val viewModel by viewModels<RecommendedViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRecommendedBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.rvYoutube.adapter = YoutubeRvAdapter()

        return binding.root
    }
}