package com.pinhsiang.fitracker.workout

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.yearMonth
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding
import com.pinhsiang.fitracker.daysOfWeekFromLocale
import com.pinhsiang.fitracker.setTextColorRes
import kotlinx.android.synthetic.main.calendar_day_layout.view.*
import kotlinx.android.synthetic.main.calendar_day_legend.*
import kotlinx.android.synthetic.main.calendar_day_legend.view.*
import kotlinx.android.synthetic.main.fragment_workout.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
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

    // For calendar view
    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Binding ViewModel
        val application = requireNotNull(activity).application
        viewModelFactory = WorkoutViewModelFactory(application)
        binding.viewModel = viewModel

        // Set calendar (native) listener.
//        binding.calendarView.setOnDateChangeListener { _, year, month, date ->
//            val day = binding.calendarView.weekDayTextAppearance
//            Log.i(TAG, "weekDayTextAppearance = $day")
//            val calDate = binding.calendarView.date
//            Log.i(TAG, "date = $calDate")
//            val calDate2 = binding.calendarView.dateTextAppearance
//            Log.i(TAG, "dateTextAppearance = $calDate2")
//            viewModel.getWorkoutDataByDate(year, month + 1, date)
//        }

        // Set workout recycler view
        val manager = LinearLayoutManager(context)
        binding.rvWorkout.layoutManager = manager
        binding.rvWorkout.adapter = WorkoutRVAdapter()

        /*
        * Set custom calendar view
        */
        // Show all days of the month indicated on screen.
        val customCalendar = binding.exOneCalendar
        val daysOfWeek = daysOfWeekFromLocale()
        binding.legendLayout.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].name.take(3).toUpperCase()
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        customCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        customCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = view.calendarDayText

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDates.contains(day.date)) {
                            selectedDates.remove(day.date)
                        } else {
                            selectedDates.add(day.date)
                        }
                        customCalendar.notifyDayChanged(day)
                    }
                }
            }
        }

        customCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        selectedDates.contains(day.date) -> {
                            textView.setBackgroundResource(R.drawable.calendar_selected_bg)
                        }
                        today == day.date -> {
                            textView.setBackgroundResource(R.drawable.calendar_today_bg)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.colorBlack)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.calendar_grey)
                    textView.background = null
                }
            }
        }

        customCalendar.monthScrollListener = {
            if (customCalendar.maxRowCount == 6) {
                exOneYearText.text = it.yearMonth.year.toString()
                exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)
            } else {
                // In week mode, we show the header a bit differently.
                // We show indices with dates from different months since
                // dates overflow and cells in one index can belong to different
                // months/years.
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    exOneYearText.text = firstDate.yearMonth.year.toString()
                    exOneMonthText.text = monthTitleFormatter.format(firstDate)
                } else {
                    exOneMonthText.text =
                        "${monthTitleFormatter.format(firstDate)} - ${monthTitleFormatter.format(lastDate)}"
                    if (firstDate.year == lastDate.year) {
                        exOneYearText.text = firstDate.yearMonth.year.toString()
                    } else {
                        exOneYearText.text = "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }

        }

        binding.weekModeCheckBox.setOnCheckedChangeListener { _, monthToWeek ->
            val firstDate = customCalendar.findFirstVisibleDay()?.date ?: return@setOnCheckedChangeListener
            val lastDate = customCalendar.findLastVisibleDay()?.date ?: return@setOnCheckedChangeListener

            val oneWeekHeight = customCalendar.dayHeight
            val oneMonthHeight = oneWeekHeight * 6

            val oldHeight = if (monthToWeek) oneMonthHeight else oneWeekHeight
            val newHeight = if (monthToWeek) oneWeekHeight else oneMonthHeight

            // Animate calendar height changes.
            val animator = ValueAnimator.ofInt(oldHeight, newHeight)
            animator.addUpdateListener { animator ->
                customCalendar.layoutParams = customCalendar.layoutParams.apply {
                    height = animator.animatedValue as Int
                }
            }

            // When changing from month to week mode, we change the calendar's
            // config at the end of the animation(doOnEnd) but when changing
            // from week to month mode, we change the calendar's config at
            // the start of the animation(doOnStart). This is so that the change
            // in height is visible. You can do this whichever way you prefer.

            animator.doOnStart {
                if (!monthToWeek) {
                    customCalendar.inDateStyle = InDateStyle.ALL_MONTHS
                    customCalendar.maxRowCount = 6
                    customCalendar.hasBoundaries = true
                }
            }
            animator.doOnEnd {
                if (monthToWeek) {
                    customCalendar.inDateStyle = InDateStyle.FIRST_MONTH
                    customCalendar.maxRowCount = 1
                    customCalendar.hasBoundaries = false
                }

                if (monthToWeek) {
                    // We want the first visible day to remain
                    // visible when we change to week mode.
                    customCalendar.scrollToDate(firstDate)
                } else {
                    // When changing to month mode, we choose current
                    // month if it is the only one in the current frame.
                    // if we have multiple months in one frame, we prefer
                    // the second one unless it's an outDate in the last index.
                    if (firstDate.yearMonth == lastDate.yearMonth) {
                        customCalendar.scrollToMonth(firstDate.yearMonth)
                    } else {
                        customCalendar.scrollToMonth(minOf(firstDate.yearMonth.next, endMonth))
                    }
                }
            }
            animator.duration = 250
            animator.start()
        }


        return binding.root
    }
}