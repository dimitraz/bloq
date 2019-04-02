package org.wit.blocky.views.profile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.entry.JournalEntry

class ProfileViewModel(application: Application) : ViewModel() {
    var app = application as MainApp
    val displayName = MutableLiveData<String>()
    val results = MutableLiveData<MutableList<JournalEntry>>()

    init {
        if (app.currentUser.displayName?.isNotEmpty()!!) {
            displayName.value = app.currentUser.displayName
        } else {
            displayName.value = "Jane Doe"
        }
    }

    fun addAll(newResults: MutableList<JournalEntry>) {
        val list = mutableListOf<JournalEntry>()
        if (results.value != null) {
            list.addAll(results.value!!)
        }
        list.addAll(newResults)
        results.value = list.asSequence().distinct().toMutableList()
    }
}

class ProfileViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(application) as T
    }
}