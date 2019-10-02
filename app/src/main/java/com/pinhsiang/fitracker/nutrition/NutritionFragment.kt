package com.pinhsiang.fitracker.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.pinhsiang.fitracker.*
import com.pinhsiang.fitracker.data.Nutrition
import com.pinhsiang.fitracker.databinding.FragmentNutritionBinding
import com.pinhsiang.fitracker.ext.daysOfWeekFromLocale
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.ext.makeInVisible
import com.pinhsiang.fitracker.ext.setTextColorRes
import kotlinx.android.synthetic.main.calendar_day_layout.view.*
import kotlinx.android.synthetic.main.fragment_nutrition.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.sql.Timestamp

class NutritionFragment : Fragment() {

    private lateinit var binding: FragmentNutritionBinding

    /**
     * Lazily initialize [NutritionViewModel].
     */
    private val viewModel by viewModels<NutritionViewModel> {getVmFactory()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentNutritionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Bind ViewModel
        binding.viewModel = viewModel

        // Setup nutrition RecyclerView
        binding.rvNutrition.adapter = NutritionRVAdapter()

        viewModel.navigationToRecord.observe(this, Observer {
            if (it) {
                val dataDate =
                    if (viewModel.selectedDate == null) viewModel.today else viewModel.selectedDate
                val dataTime = Timestamp.valueOf("$dataDate $ZERO_HOUR").time
                val nutrition = Nutrition(
                    time = dataTime
                )
                this.findNavController()
                    .navigate(NutritionFragmentDirections.ActionNutritionFragmentToNutritionRecordFragment(nutrition))
                viewModel.addNewDataDone()
            }
        })

        viewModel.downloadComplete.observe(this, Observer {
            if (it) {
                binding.customCalendar.notifyCalendarChanged()
                viewModel.refreshDataDone()
            }
        })

        setCustomCalendar()

        return binding.root
    }

    private fun setCustomCalendar() {
        val customCalendar = binding.customCalendar

        // Show all days of week, the 1st day of week is sunday.
        val daysOfWeek = daysOfWeekFromLocale()
        binding.legendLayout.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].name.take(3).toUpperCase()
            }
        }

        val currentMonth = YearMonth.now()
        customCalendar.setup(
            currentMonth.minusMonths(36),
            currentMonth,
            daysOfWeek.first()
        )
        customCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = view.calendarDayText
            val dotView = view.calendarDotView

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectDate(day.date)
                        viewModel.getNutritionDataByDate(day.date)
                        viewModel.setDataStatusByDate(day.date)
//                        val selectDateToTimestamp = Timestamp.valueOf(day.date.toString() + " 00:00:00").time
//                        val timestampToday = Timestamp.valueOf(LocalDate.now().toString() + " 00:00:00").time
//                        Log.i(TAG, "day.date = ${day.date}")
//                        Log.i(TAG, "selectDateToTimestamp = $selectDateToTimestamp")
//                        Log.i(TAG, "selectDate = ${selectDateToTimestamp.timestampToString()}")
//                        Log.i(TAG, "timestampToday = $timestampToday")
//                        Log.i(TAG, "Today = ${timestampToday.timestampToString()}")
//                        Log.i(TAG, "System.currentTime = ${System.currentTimeMillis()}")
//                        Log.i(TAG, "System.currentTime = ${System.currentTimeMillis().timestampToString()}")
                    }
                }
            }
        }

        customCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val dotView = container.dotView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    when (day.date) {
                        viewModel.today -> {
                            textView.setTextColorRes(R.color.colorText)
                            textView.setBackgroundResource(R.drawable.calendar_today_bg)
                            dotView.isVisible = viewModel.hasNutritionData(day.date)
                        }
                        viewModel.selectedDate -> {
                            textView.setTextColorRes(R.color.colorText)
                            textView.setBackgroundResource(R.drawable.calendar_selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.colorText)
                            textView.background = null
                            dotView.isVisible = viewModel.hasNutritionData(day.date)
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.colorItem)
                    dotView.isVisible = viewModel.hasNutritionData(day.date)
//                    textView.background = null
                }
            }
        }

        // Change title when scroll calendar
        customCalendar.monthScrollListener = {
            text_year.text = it.yearMonth.year.toString()
            text_month.text = DateTimeFormatter.ofPattern(com.pinhsiang.fitracker.workout.MONTH_TITLE_FORMATTER).format(it.yearMonth)
        }

        // Expand or compress the calendar when click the title of calendar
        binding.titleCalendar.setOnClickListener {
            if (viewModel.calendarExpanding) {
                binding.customCalendar.visibility = View.GONE
                binding.legendLayout.visibility = View.GONE
                binding.indicator.background =
                    FitrackerApplication.appContext.getDrawable(R.drawable.ic_calendar_arrow_down_24dp)
                viewModel.calendarExpanding = false
            } else {
                binding.customCalendar.visibility = View.VISIBLE
                binding.legendLayout.visibility = View.VISIBLE
                binding.indicator.background =
                    FitrackerApplication.appContext.getDrawable(R.drawable.ic_calendar_arrow_up_24dp)
                viewModel.calendarExpanding = true
            }
        }
    }

    private fun selectDate(date: LocalDate) {
        if (viewModel.selectedDate != date) {
            val oldDate = viewModel.selectedDate
            viewModel.selectedDate = date
            oldDate?.let { binding.customCalendar.notifyDateChanged(it) }
            binding.customCalendar.notifyDateChanged(date)
        }
    }
}