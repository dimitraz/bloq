package org.wit.blocky.views.entry

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wit.blocky.helpers.imageIntent
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.JournalEntry

class EntryViewModel(application: Application, val date: CalendarDate) : ViewModel() {
    var entry: JournalEntry
    var app = application as MainApp
    val IMAGE_REQUEST = 1

    init {
        if (app.entries.findByDate(date) != null) {
            entry = app.entries.findByDate(date)!!
        } else {
            entry = JournalEntry(date = date)
            app.entries.create(entry)
        }
    }

    fun saveEntry() {
        app.entries.update(entry)
    }

    fun selectImage(fragment: EntryFragment) {
        fragment.startActivityForResult(imageIntent(), IMAGE_REQUEST)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                entry.image = data.data.toString()
            }
        }
    }
}

class EntryViewModelFactory(private val application: Application, private val date: CalendarDate) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntryViewModel(application, date) as T
    }
}