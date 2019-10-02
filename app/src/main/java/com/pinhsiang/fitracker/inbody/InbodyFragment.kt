package com.pinhsiang.fitracker.inbody

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
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.Inbody
import com.pinhsiang.fitracker.databinding.FragmentInbodyBinding
import com.pinhsiang.fitracker.ext.daysOfWeekFromLocale
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.ext.makeInVisible
import com.pinhsiang.fitracker.ext.setTextColorRes
import kotlinx.android.synthetic.main.calendar_day_layout.view.*
import kotlinx.android.synthetic.main.fragment_inbody.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import java.sql.Timestamp

class InbodyFragment : Fragment() {

    private lateinit var binding: FragmentInbodyBinding

    /**
     * Lazily initialize [InbodyViewModel].
     */
    private val viewModel by viewModels<InbodyViewModel> {getVmFactory()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInbodyBinding.inflate(inflater, container, false)


        // Bind ViewModel, life cycle owner and calendarStatus(Check if calendar is expending or not).
        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = this@InbodyFragment
        }

        viewModel.navigationToRecord.observe(this, Observer {
            if (it) {
                val dataDate =
                    if (viewModel.selectedDate == null) viewModel.today else viewModel.selectedDate
                val dataTime = Timestamp.valueOf("$dataDate $ZERO_HOUR").time
                val inbody = Inbody(
                    time = dataTime
                )
                this.findNavController()
                    .navigate(InbodyFragmentDirections.actionInbodyFragmentToInbodyRecordFragment(inbody))
                viewModel.addNewDataDone()
            }
        })

        // Expand or compress the calendar when click the title of calendar
        binding.titleCalendar.setOnClickListener {
            if (viewModel.calendarExpanding.value!!) {
                binding.customCalendar.visibility = View.GONE
                binding.legendLayout.visibility = View.GONE
                viewModel.setCalendarExpandingFalse()
            } else {
                binding.customCalendar.visibility = View.VISIBLE
                binding.legendLayout.visibility = View.VISIBLE
                viewModel.setCalendarExpandingTrue()
            }
        }

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
                        viewModel.getInbodyDataByDate(day.date)
//                        val selectDateToTimestamp = Timestamp.valueOf(day.date.toString() + " 00:00:00").time
//                        val timestampToday = Timestamp.valueOf(LocalDate.now().toString() + " 00:00:00").time
//                        Log.i(com.pinhsiang.fitracker.nutrition.TAG, "day.date = ${day.date}")
//                        Log.i(com.pinhsiang.fitracker.nutrition.TAG, "selectDateToTimestamp = $selectDateToTimestamp")
//                        Log.i(com.pinhsiang.fitracker.nutrition.TAG, "selectDate = ${selectDateToTimestamp.timestampToString()}")
//                        Log.i(com.pinhsiang.fitracker.nutrition.TAG, "timestampToday = $timestampToday")
//                        Log.i(com.pinhsiang.fitracker.nutrition.TAG, "Today = ${timestampToday.timestampToString()}")
//                        Log.i(com.pinhsiang.fitracker.nutrition.TAG, "System.currentTime = ${System.currentTimeMillis()}")
//                        Log.i(com.pinhsiang.fitracker.nutrition.TAG, "System.currentTime = ${System.currentTimeMillis().timestampToString()}")
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
                            dotView.isVisible = viewModel.hasInbodyData(day.date)
                        }
                        viewModel.selectedDate -> {
                            textView.setTextColorRes(R.color.colorText)
                            textView.setBackgroundResource(R.drawable.calendar_selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.colorText)
                            textView.background = null
                            dotView.isVisible = viewModel.hasInbodyData(day.date)
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.colorItem)
                    dotView.isVisible = viewModel.hasInbodyData(day.date)
//                    textView.background = null
                }
            }
        }

        // Change title when scroll calendar
        customCalendar.monthScrollListener = {
            text_year.text = it.yearMonth.year.toString()
            text_month.text = DateTimeFormatter.ofPattern(com.pinhsiang.fitracker.workout.MONTH_TITLE_FORMATTER).format(it.yearMonth)
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