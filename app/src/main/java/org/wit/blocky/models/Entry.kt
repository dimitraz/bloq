package org.wit.blocky.models

import com.prolificinteractive.materialcalendarview.CalendarDay

data class Entry(
    var bookmarked: Boolean = false,
    var calendarDay: CalendarDay,
    var notes: String
)