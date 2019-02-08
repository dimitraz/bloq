package org.wit.blocky.models

import com.prolificinteractive.materialcalendarview.CalendarDay

data class JournalEntry(
    var title: String = "",
    var bookmarked: Boolean = false,
    var calendarDay: CalendarDay,
    var notes: String
)