package org.wit.blocky.models

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.io.Serializable

data class JournalEntry(
    var title: String = "",
    var bookmarked: Boolean = false,
    var date: CalendarDay?,
    var prompts: HashMap<String, String>? = null,
    var notes: String? = "",
    var image: String = "",
    var category: String = ""
) : Serializable