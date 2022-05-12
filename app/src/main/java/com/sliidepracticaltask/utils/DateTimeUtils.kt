package com.sliidepracticaltask.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

//For opening date picker dialog
fun Context.openDatePicker(
    preSelectedDate: Calendar = Calendar.getInstance(),
    pastDisableDate: Calendar? = null,
    preDisableDate: Calendar? = null,
    callback: (Calendar) -> Unit
) {
    val dateDialog = DatePickerDialog(
        this,
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            callback(selectedDate)
        },
        preSelectedDate.get(Calendar.YEAR),
        preSelectedDate.get(Calendar.MONTH),
        preSelectedDate.get(Calendar.DATE)
    )
    pastDisableDate?.let {
        //dateDialog.datePicker.minDate = preSelectedDate.timeInMillis - (preSelectedDate.timeInMillis %(24*60*60*1000))
        dateDialog.datePicker.minDate =
            it.timeInMillis //- (preSelectedDate.timeInMillis % (24 60 60 * 1000))
    }
    preDisableDate?.let {
        dateDialog.datePicker.maxDate =
            it.timeInMillis// - (preSelectedDate.timeInMillis % (24 60 60 * 1000))
    }

    dateDialog.show()
}

//For opening time picker dialog
fun Context.openTimePicker(
    preSelectedDate: Calendar = Calendar.getInstance(),
    callback: (Calendar) -> Unit
) {
    val dateDialog = TimePickerDialog(
        this,
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(
                preSelectedDate.get(Calendar.YEAR),
                preSelectedDate.get(Calendar.MONTH),
                preSelectedDate.get(Calendar.DATE),
                hourOfDay, minute
            )
            callback(selectedDate)
        },
        preSelectedDate.get(Calendar.HOUR_OF_DAY),
        preSelectedDate.get(Calendar.MINUTE),
        true
    )


    dateDialog.show()
}

//For formatting date to any form
fun Calendar.formatTo(format: String): String =
    try {
        val dateFormat = SimpleDateFormat(format, Locale.US)
        dateFormat.format(this.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

fun Date.formatTo(format: String): String =
    try {
        val dateFormat = SimpleDateFormat(format, Locale.US)
        dateFormat.format(this.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

//For formatting date to any form
fun Long.formatTo(format: String): String =
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        val dateFormat = SimpleDateFormat(format, Locale.US)
        dateFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

fun String.toCalendar(format: String): Calendar {
    val cal = Calendar.getInstance()
    if (this.isNotEmpty())
        SimpleDateFormat(format, Locale.US).parse(this).apply {
            this?.let {
                cal.time = it
            }
        }
    return cal
}


fun Fragment.getTimeMessage(date: String, format: String): String {
    val df = SimpleDateFormat(format, Locale.ENGLISH)
    df.timeZone = TimeZone.getTimeZone("UTC")
    val date = df.parse(date).time
    df.timeZone = TimeZone.getDefault()
    val formattedDate = df.format(date)

    val now = System.currentTimeMillis()

    val ago = DateUtils.getRelativeTimeSpanString(
        date, now,
        DateUtils.MINUTE_IN_MILLIS
    )

    return ago.toString()
}

fun Fragment.getFormattedDate(date: String, fromFormat: String, toFormat: String): String {
    val fromDf = SimpleDateFormat(fromFormat, Locale.ENGLISH)
    fromDf.timeZone = TimeZone.getTimeZone("UTC");

    val fromDate = fromDf.parse(date).time
    return SimpleDateFormat(toFormat, Locale.ENGLISH).apply {
        timeZone = TimeZone.getDefault()
    }.format(fromDate)
}


