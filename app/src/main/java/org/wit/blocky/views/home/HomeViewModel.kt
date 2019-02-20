package org.wit.blocky.views.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.JournalEntry

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val allEntries: MutableList<JournalEntry> = mutableListOf()
    val entries: MutableList<JournalEntry> = mutableListOf()
    var categories: MutableList<String> = mutableListOf()
    var app = application as MainApp

    init {
        allEntries.add(
            JournalEntry(
                title = "New Entry 1",
                bookmarked = true,
                notes = "1 Test entry",
                date = CalendarDate(CalendarDay.today()),
                image = "https://www.sciencemag.org/sites/default/files/styles/inline__450w__no_aspect/public/Taiwan_16x9.jpg?itok=uXcThZKL",
                category = app.categories[1]
            )
        )

        allEntries.add(
            JournalEntry(
                title = "New Entry 2",
                bookmarked = true,
                notes = "2 Test entry",
                date = CalendarDate(CalendarDay.from(2019, 10, 3)),
                category = app.categories[0]
            )
        )

        allEntries.add(
            JournalEntry(
                title = "New Entry 3",
                bookmarked = true,
                notes = "3 Test entry",
                date = CalendarDate(CalendarDay.from(2018, 3, 4)),
                category = app.categories[3]
            )
        )

        allEntries.add(
            JournalEntry(
                title = "New Entry 4",
                bookmarked = true,
                notes = "4 Test entry",
                date = CalendarDate(CalendarDay.from(2018, 2, 2)),
                category = app.categories[3],
                image = "hello"
            )
        )


        entries.addAll(allEntries)
    }
}
