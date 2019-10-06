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
import androidx.navigation.fragment.findNavController
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.pinhsiang.fitracker.MONTH_TITLE_FORMATTER
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.ZERO_HOUR
import com.pinhsiang.fitracker.data.InBody
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

class InBodyFragment : Fragment() {

    private lateinit var binding: FragmentInbodyBinding

    /**
     * Lazily initialize [InBodyViewModel].
     */
    private val viewModel by viewModels<InBodyViewModel> { getVmFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInbodyBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigationToRecord.observe(this, Observer {

            if (it) {

                val dataDate =
                    if (viewModel.selectedDate == null) viewModel.today else viewModel.selectedDate

                val dataTime = Timestamp.valueOf("$dataDate $ZERO_HOUR").time

                val inBody = InBody(
                    time = dataTime
                )

                this.findNavController()
                    .navigate(InBodyFragmentDirections.actionInbodyFragmentToInbodyRecordFragment(inBody))
                viewModel.addNewDataDone()
            }
        })

        /**
         *  Expand or compress the calendar when click the title of calendar
         */
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

        viewModel.downloadComplete.observe(this, Observer { downloadComplete ->

            if (downloadComplete) {
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
                        viewModel.refreshInBodyDataByDate(day.date)
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
                            dotView.isVisible = viewModel.hasInBodyData(day.date)
                        }

                        viewModel.selectedDate -> {
                            textView.setTextColorRes(R.color.colorText)
                            textView.setBackgroundResource(R.drawable.calendar_selected_bg)
                            dotView.makeInVisible()
                        }

                        else -> {
                            textView.setTextColorRes(R.color.colorText)
                            textView.background = null
                            dotView.isVisible = viewModel.hasInBodyData(day.date)
                        }
                    }
                } else {

                    textView.setTextColorRes(R.color.colorItem)
                    dotView.isVisible = viewModel.hasInBodyData(day.date)
                }
            }
        }

        // Change title when scroll calendar
        customCalendar.monthScrollListener = {
            text_year.text = it.yearMonth.year.toString()
            text_month.text = DateTimeFormatter.ofPattern(MONTH_TITLE_FORMATTER).format(it.yearMonth)
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