package org.wit.blocky.views.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.JournalEntry

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    var allEntries: List<JournalEntry> = listOf()
    var entries: MutableList<JournalEntry> = mutableListOf()
    var categories: MutableList<String> = mutableListOf()
    var app = application as MainApp

    init {
        allEntries = app.entries.findAll()
        entries.addAll(allEntries)
    }
}
