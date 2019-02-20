package org.wit.blocky.main

import android.app.Application
import org.wit.blocky.models.store.FirebaseStore


class MainApp : Application() {
    // Create map of entries, load entry
    // by searching date key in map
    lateinit var template: MutableList<String>
    lateinit var categories: MutableList<String>
    lateinit var entries: FirebaseStore

    override fun onCreate() {
        super.onCreate()

        entries = FirebaseStore(applicationContext)

        template = mutableListOf(
            "Question 1",
            "Question 2",
            "Question 3",
            "Question 4",
            "Question 5"
        )

        categories = mutableListOf(
            "Lifestyle",
            "Personal",
            "Travel",
            "Favourites"
        )
    }
}