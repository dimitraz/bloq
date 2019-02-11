package org.wit.blocky.views.home

import androidx.lifecycle.ViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.wit.blocky.models.JournalEntry

class HomeViewModel : ViewModel() {
    val entries: MutableList<JournalEntry> = mutableListOf()

    init {
        for (i in 1..5) {
            entries.add(
                JournalEntry(
                    title = "New Entry $i",
                    bookmarked = true,
                    notes = "$i Test entry",
                    date = CalendarDay.today(),
                    image = "https://www.sciencemag.org/sites/default/files/styles/inline__450w__no_aspect/public/Taiwan_16x9.jpg?itok=uXcThZKL"
                )
            )
        }

        for (i in 5..10) {
            entries.add(
                JournalEntry(
                    title = "New Entry $i",
                    bookmarked = true,
                    notes = "$i Test entry",
                    date = CalendarDay.today()
                )
            )
        }
    }
}
