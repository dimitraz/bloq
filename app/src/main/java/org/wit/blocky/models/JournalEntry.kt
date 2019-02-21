package org.wit.blocky.models

import java.io.Serializable

data class JournalEntry(
    var fbId: String = "",
    var title: String = "",
    var bookmarked: Boolean = false,
    var date: CalendarDate = CalendarDate(),
    var prompts: MutableMap<String, String> = mutableMapOf(),
    var notes: String? = "",
    var image: String = "",
    var category: String = "Personal"
) : Serializable