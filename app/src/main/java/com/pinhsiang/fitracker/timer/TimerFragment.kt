package com.pinhsiang.fitracker.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.MainActivity
import com.pinhsiang.fitracker.databinding.FragmentTimerBinding
import kotlinx.android.synthetic.main.activity_main.*

class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding

    /**
     * Lazily initialize [TimerViewModel].
     */
    private lateinit var viewModelFactory: TimerViewModelFactory
    private val viewModel: TimerViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TimerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as MainActivity).bottom_nav_view.visibility = View.GONE

        binding = FragmentTimerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Bind ViewModel
        val application = requireNotNull(activity).application
        viewModelFactory = TimerViewModelFactory(application)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).bottom_nav_view.visibility = View.VISIBLE
    }
}