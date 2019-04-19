package org.wit.blocky.views.tags

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.entry.FirebaseStore

class TagViewModel(application: Application) : ViewModel() {
    var app = application as MainApp
    val tags = MutableLiveData<MutableList<String>>()

    init {
        if (app.currentUser.tags.isNotEmpty()) {
            tags.value = app.currentUser.tags
        }
    }

    fun addTag(tag: String) {
        Log.i("Bloq", "Adding new tag: $tag, ${tags.value}")

        val list = mutableListOf<String>()
        if (tags.value != null) {
            list.addAll(tags.value!!)
        }
        if (!list.contains(tag)) {
            list.add(tag)
        }

        app.currentUser.tags.add(tag)
        app.users.update(app.currentUser)
        tags.value = list.asSequence().distinct().toMutableList()
    }

    fun deleteTag(tag: String, fireStore: FirebaseStore) {
        Log.i("Bloq", "Deleting tag: $tag, ${tags.value}")

        val list = mutableListOf<String>()
        if (tags.value != null) {
            list.addAll(tags.value!!)
        }
        if (list.contains(tag)) {
            list.remove(tag)
        }

        app.currentUser.tags.remove(tag)
        fireStore.fetchAllEntries(app.currentUser.authId) {
            for (item in fireStore.allEntries) {
                if (item.category == tag) {
                    item.category = ""
                    fireStore.update(item)
                }
            }
        }

        app.users.update(app.currentUser)
        tags.value = list.asSequence().distinct().toMutableList()
    }

}

class TagViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TagViewModel(application) as T
    }
}