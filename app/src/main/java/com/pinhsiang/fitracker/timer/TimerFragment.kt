package com.pinhsiang.fitracker.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.pinhsiang.fitracker.Int2StringConverter
import com.pinhsiang.fitracker.MainActivity
import com.pinhsiang.fitracker.databinding.FragmentTimerBinding
import com.pinhsiang.fitracker.ext.getVmFactory

const val TAG = "Fitracker"

class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding

    /**
     * Lazily initialize [TimerViewModel].
     */
    private val viewModel by viewModels<TimerViewModel> {getVmFactory()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTimerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.converter = Int2StringConverter

        // Bind ViewModel
        binding.viewModel = viewModel

        binding.rvTimerPattern.adapter = TimerPatternRvAdapter(viewModel)

        viewModel.timerPatternList.observe(this, Observer {
            (binding.rvTimerPattern.adapter as TimerPatternRvAdapter).notifyDataSetChanged()
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

        viewModel.timerStart.observe(this, Observer {
            when (it) {
                true -> (activity as MainActivity).window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                false -> (activity as MainActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        })

        viewModel.addToTimerPatternList.observe(this, Observer {
            if (it) {
                binding.rvTimerPattern.scrollToPosition(viewModel.endOfTimerPatternList())
                viewModel.setAddToTimerPatternListFalse()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (viewModel.timerStart.value == true) {
            viewModel.intervalTimer.cancel()
        }
    }
}