package org.wit.blocky.helpers

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.io.Serializable

class CalendarHelpers {
    fun dateToString(date: CalendarDay): String {
        val month = when (date.month) {
            0 -> "January"
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
            else -> ""
        }
        return "${date.day} $month"
    }
}

data class CalendarDate(val day: CalendarDay) : Serializable {
    fun dateToString(): String {
        val month = when (day.month) {
            0 -> "January"
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
            else -> ""
        }
        return "${day.day} $month"
    }
}