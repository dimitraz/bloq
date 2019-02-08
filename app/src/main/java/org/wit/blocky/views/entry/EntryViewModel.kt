package org.wit.blocky.views.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wit.blocky.models.JournalEntry

class EntryViewModel(val entry: JournalEntry) : ViewModel()

class EntryViewModelFactory(private val entry: JournalEntry) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EntryViewModel(entry) as T
    }
}