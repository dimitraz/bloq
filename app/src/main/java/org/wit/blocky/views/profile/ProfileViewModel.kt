package org.wit.blocky.views.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.blocky.models.entry.JournalEntry

class ProfileViewModel : ViewModel() {
    val results = MutableLiveData<MutableList<JournalEntry>>()

    fun addAll(newResults: MutableList<JournalEntry>) {
        val list = mutableListOf<JournalEntry>()
        if (results.value != null) {
            list.addAll(results.value!!)
        }
        list.addAll(newResults)
        results.value = list.asSequence().distinct().toMutableList()
    }
}
