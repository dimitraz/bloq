package org.wit.blocky.views.home

import androidx.lifecycle.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.wit.blocky.models.JournalEntry

class HomeViewModel : ViewModel() {
    val entries: MutableList<JournalEntry> = mutableListOf()

    init {
        for (i in 1..10) {
            entries.add(
                JournalEntry(
                    title = "New Entry $i",
                    bookmarked = true,
                    notes = "Test entry",
                    date = CalendarDay.today()
                )
            )
        }
    }
}
