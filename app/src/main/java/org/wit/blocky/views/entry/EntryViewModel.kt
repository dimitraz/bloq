package org.wit.blocky.views.entry

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.JournalEntry

class EntryViewModel(application: Application, val date: CalendarDate) : ViewModel() {
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

class EntryViewModelFactory(private val application: Application, private val date: CalendarDate) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntryViewModel(application, date) as T
    }
}