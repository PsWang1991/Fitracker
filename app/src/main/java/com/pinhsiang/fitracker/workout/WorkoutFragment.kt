package com.pinhsiang.fitracker.workout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding
import java.util.*

class WorkoutFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding

    /**
     * Lazily initialize [WorkoutViewModel].
     */
    private lateinit var viewModelFactory: WorkoutViewModelFactory
    private val viewModel: WorkoutViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(WorkoutViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Binding ViewModel
        val application = requireNotNull(activity).application
        viewModelFactory = WorkoutViewModelFactory(application)
        binding.viewModel = viewModel

        // Set calendar listener.
        binding.calendarView.setOnDateChangeListener { _, year, month, date ->
            val day = binding.calendarView.weekDayTextAppearance
            Log.i(TAG, "weekDayTextAppearance = $day")
            val calDate = binding.calendarView.date
            Log.i(TAG, "date = $calDate")
            val calDate2 = binding.calendarView.dateTextAppearance
            Log.i(TAG, "dateTextAppearance = $calDate2")
            viewModel.getWorkoutDataByDate(year, month + 1, date)
        }


        // Set workout recycler view
        val manager = LinearLayoutManager(context)
        binding.rvWorkout.layoutManager = manager
        binding.rvWorkout.adapter = WorkoutRVAdapter()


        return binding.root
    }
}