package org.wit.blocky.views.entry

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wit.blocky.helpers.imageIntent
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.JournalEntry

class EntryViewModel(application: Application, val date: CalendarDate) : ViewModel() {

    var entry: JournalEntry
    var app = application as MainApp
    private val IMAGE_REQUEST = 1

    init {
        if (app.entries.findByDate(date) != null) {
            entry = app.entries.findByDate(date)!!
        } else {
            entry = JournalEntry(date = date)
            app.template.forEach {
                entry.prompts[it] = ""
            }
            app.entries.create(entry)
        }
    }

    fun saveEntry() {
        app.entries.update(entry)
    }

    fun deleteEntry() {
        app.entries.delete(entry)
    }

    fun selectImage(fragment: EntryFragment) {
        fragment.startActivityForResult(imageIntent(), IMAGE_REQUEST)
    }
}

class EntryViewModelFactory(private val application: Application, private val date: CalendarDate) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntryViewModel(application, date) as T
    }
}