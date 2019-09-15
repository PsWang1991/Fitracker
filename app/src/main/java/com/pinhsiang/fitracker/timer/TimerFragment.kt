package com.pinhsiang.fitracker.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.Int2StringConverter
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
        binding.converter = Int2StringConverter

        // Bind ViewModel
        val application = requireNotNull(activity).application
        viewModelFactory = TimerViewModelFactory(application)
        binding.viewModel = viewModel

        binding.rvTimerPattern.adapter = TimerPatternRVAdapter(viewModel)

        viewModel.timerPatternList.observe(this, Observer {
            (binding.rvTimerPattern.adapter as TimerPatternRVAdapter).notifyDataSetChanged()
        })

        viewModel.displayExerciseMinutes.observe(this, Observer {
            if (viewModel.displayExerciseSeconds.value!! >= 1) {
                if (it < 0) {
                    viewModel.setDisplayExerciseMinutesTo0()
                }
            } else {
                if (it < 1) {
                    viewModel.setDisplayExerciseMinutesTo1()
                }
            }

            if (it > 59) {
                viewModel.setDisplayExerciseMinutesTo59()
            }
        })

        viewModel.displayExerciseSeconds.observe(this, Observer {
            if (viewModel.displayExerciseMinutes.value!! >= 1) {
                if (it < 0) {
                    viewModel.setDisplayExerciseSecondsTo0()
                }
            } else {
                if (it < 1) {
                    viewModel.setDisplayExerciseSecondsTo1()
                }
            }
            if (it > 59) {
                viewModel.setDisplayExerciseSecondsTo59()
            }
        })

        viewModel.displayRestMinutes.observe(this, Observer {
            if (viewModel.displayRestSeconds.value!! >= 1) {
                if (it < 0) {
                    viewModel.setDisplayRestMinutesTo0()
                }
            } else {
                if (it < 1) {
                    viewModel.setDisplayRestMinutesTo1()
                }
            }

            if (it > 59) {
                viewModel.setDisplayRestMinutesTo59()
            }
        })

        viewModel.displayRestSeconds.observe(this, Observer {
            if (viewModel.displayRestMinutes.value!! >= 1) {
                if (it < 0) {
                    viewModel.setDisplayRestSecondsTo0()
                }
            } else {
                if (it < 1) {
                    viewModel.setDisplayRestSecondsTo1()
                }
            }
            if (it > 59) {
                viewModel.setDisplayRestSecondsTo59()
            }
        })

        viewModel.patternRepeats.observe(this, Observer {
            if (it < 1) {
                viewModel.setPatternRepeatsTo1()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).bottom_nav_view.visibility = View.VISIBLE
    }
}