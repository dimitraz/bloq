package org.wit.blocky.models

import java.io.Serializable

data class CalendarDate(val day: Int = 1, val month: Int = 1, val year: Int = 2019) : Serializable {
    fun getDate(): String {
        return "$day-$month-$year"
    }

    override fun toString(): String {
        val month = when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> ""
        }
        return "$day $month"
    }
}