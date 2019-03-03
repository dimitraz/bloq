package org.wit.blocky.models.store

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.JournalEntry

class FirebaseStore(val context: Context) : JournalStore {

    val entries = ArrayList<JournalEntry>()
    private lateinit var db: DatabaseReference

    override fun findAll(): List<JournalEntry> {
        return entries
    }

    override fun findByDate(date: CalendarDate): JournalEntry? {
        return entries.find { p -> p.date.getDate() == date.getDate() }
    }

    fun getLatest(): List<JournalEntry> {
        val today = CalendarDay.today()
        val lastWeek = CalendarDay.from(LocalDate.now().minusDays(7))

        return entries.filter { p ->
            CalendarDay.from(p.date.year, p.date.month, p.date.day).isInRange(lastWeek, today)
        }
    }

    override fun create(entry: JournalEntry) {
        Log.d("Bloq", "Adding entry: $entry")

        val key = db.child("entries").push().key
        key?.let {
            entry.fbId = key
            entries.add(entry)
            db.child("entries").child(key).setValue(entry)
        }
        Log.d("Bloq", "Adding entry: ${entry.fbId}")
    }

    override fun update(entry: JournalEntry) {
        Log.d("Bloq", "Updating entry: ${entry.fbId} $entry")
        db.child("entries").child(entry.fbId).setValue(entry)
    }

    override fun delete(entry: JournalEntry) {
        Log.d("Bloq", "Deleting entry: ${entry.fbId}")
        db.child("entries").child(entry.fbId).removeValue()
        entries.remove(entry)
    }

    fun fetchEntries(entriesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(entries) { it.getValue<JournalEntry>(JournalEntry::class.java) }
                entriesReady()
            }
        }

        db = FirebaseDatabase.getInstance().reference
        entries.clear()
        db.child("entries").addListenerForSingleValueEvent(valueEventListener)
    }
}