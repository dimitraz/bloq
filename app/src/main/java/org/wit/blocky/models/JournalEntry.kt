package org.wit.blocky.models

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.io.Serializable

data class JournalEntry(
    var title: String = "",
    var bookmarked: Boolean = false,
    var calendarDay: CalendarDay,
    var notes: String
) : Serializable