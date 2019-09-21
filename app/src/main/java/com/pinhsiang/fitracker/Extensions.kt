package com.pinhsiang.fitracker

import android.content.Context
import android.text.format.DateFormat
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import java.util.*


fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun Long.timestampToString(): String {
    return DateFormat.format(
        FitrackerApplication.appContext.getString(R.string.all_date_format),
        Calendar.getInstance().apply { timeInMillis = this@timestampToString }
    ).toString()
}

fun Long.timestampToDate(): String {
    return DateFormat.format(
        FitrackerApplication.appContext.getString(R.string.short_date_format),
        Calendar.getInstance().apply { timeInMillis = this@timestampToDate }
    ).toString()
}

fun Int.secondsIntToTime() : String {
    return String.format("%02d:%02d", this / 60, this % 60)
}

fun Float.digits(digits: Int) :String {
    return String.format("%.${digits}f", this)
}

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))