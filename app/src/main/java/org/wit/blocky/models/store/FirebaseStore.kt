package org.wit.blocky.models.store

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.JournalEntry

class FirebaseStore(val context: Context) : JournalStore {

    val entries = ArrayList<JournalEntry>()
    private lateinit var db: DatabaseReference

    override fun findAll(): List<JournalEntry> {
        return entries
    }

    override fun findByDate(date: CalendarDate): JournalEntry? {
        return entries.find { p -> p.date.date == date.date }
    }

    override fun create(entry: JournalEntry) {
        val key = db.child("entries").push().key
        key?.let {
            entry.fbId = key
            entries.add(entry)
            db.child("entries").child(key).setValue(entry)
        }
        Log.d("Bloq", "Adding entry: ${entry.fbId}")
    }

    override fun update(entry: JournalEntry) {
        Log.d("Bloq", "Updating entry: ${entry.fbId}")
    }

    override fun delete(entry: JournalEntry) {
        Log.d("Bloq", "Deleting entry: ${entry.fbId}")
    }

}