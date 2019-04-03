package org.wit.blocky.views.entry

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.wit.blocky.helpers.imageIntent
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.entry.JournalEntry

class EntryViewModel(application: Application, val bundle: Bundle?) : ViewModel() {

    var entry: JournalEntry? = null
    var app = application as MainApp
    private val IMAGE_REQUEST = 1

    init {
        var date = CalendarDate(CalendarDay.today().day, CalendarDay.today().month, CalendarDay.today().year)

        if (bundle != null) {
            if (bundle.getSerializable("entry") != null) {
                entry = bundle.getSerializable("entry") as JournalEntry
            } else if (bundle.getSerializable("date") != null) {
                date = bundle.getSerializable("date") as CalendarDate
            }
        }

        if (entry == null) {
            if (app.entries.findByDate(date) != null) {
                entry = app.entries.findByDate(date)!!
            } else {
                entry = JournalEntry(date = date)
                app.template.forEach {
                    entry!!.prompts[it] = ""
                }
                app.entries.create(entry!!)
            }
        }
    }

    fun saveEntry() {
        app.entries.update(entry!!)
    }

    fun deleteEntry() {
        app.entries.delete(entry!!)
    }

    fun selectImage(fragment: EntryFragment) {
        fragment.startActivityForResult(imageIntent(), IMAGE_REQUEST)
    }
}

class EntryViewModelFactory(private val application: Application, private val bundle: Bundle?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntryViewModel(application, bundle) as T
    }
}