package org.wit.blocky.views.entry

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.JournalEntry

class EntryViewModel(application: Application, val date: CalendarDay) : ViewModel() {
    var entry: JournalEntry
    var app = application as MainApp

    init {
        entry = if (app.entries.findByDate(date) != null) {
            app.entries.findByDate(date)!!
        } else {
            JournalEntry(date = date)
        }
    }
}

class EntryViewModelFactory(private val application: Application, private val date: CalendarDay) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntryViewModel(application, date) as T
    }
}