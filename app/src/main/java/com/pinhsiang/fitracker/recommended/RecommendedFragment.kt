package com.pinhsiang.fitracker.recommended

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.databinding.FragmentRecommendedBinding

class RecommendedFragment : Fragment() {
    lateinit var binding: FragmentRecommendedBinding

    /**
     * Lazily initialize our [RecommendedViewModel].
     */
    private val viewModel: RecommendedViewModel by lazy {
        ViewModelProviders.of(this).get(RecommendedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecommendedBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.rvYoutube.adapter = YoutubeRecyclerAdapter()

        return binding.root
    }
}