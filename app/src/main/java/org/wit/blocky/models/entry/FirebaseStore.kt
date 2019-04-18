package org.wit.blocky.models.entry

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import org.wit.blocky.helpers.readImageFromPath
import org.wit.blocky.models.CalendarDate
import java.io.ByteArrayOutputStream
import java.io.File

class FirebaseStore(val context: Context) : JournalStore {

    val entries = ArrayList<JournalEntry>()
    val allEntries = ArrayList<JournalEntry>()
    private lateinit var db: DatabaseReference
    private lateinit var st: StorageReference
    private lateinit var userId: String

    override fun findAll(): List<JournalEntry> {
        return entries
    }

    fun findAllEntries(): List<JournalEntry> {
        return allEntries
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
        entry.authorId = userId

        val name = FirebaseAuth.getInstance().currentUser!!.displayName
        if (name != null) {
            entry.authorName = FirebaseAuth.getInstance().currentUser!!.displayName!!
        } else {
            entry.authorName = "Jane Doe"
        }

        val key = db.child("entries").push().key
        key?.let {
            entry.fbId = key
            entries.add(entry)
            db.child("journals").child(userId).child("entries").child(key).setValue(entry)
        }
        updateImage(entry)

        Log.d("Bloq", "Adding entry: ${entry.fbId}")
    }

    override fun update(entry: JournalEntry) {
        Log.d("Bloq", "Updating entry: ${entry.fbId} $entry")
        db.child("journals").child(userId).child("entries").child(entry.fbId).setValue(entry)
        if (entry.image.isNotEmpty() && (entry.image[0] != 'h')) {
            updateImage(entry)
        }
    }

    override fun delete(entry: JournalEntry) {
        Log.d("Bloq", "Deleting entry: ${entry.fbId}")
        db.child("journals").child(userId).child("entries").child(entry.fbId).removeValue()
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

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        entries.clear()
        db.child("journals").child(userId).child("entries").addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchAllEntries(id: String, entriesReady: () -> Unit) {
        Log.d("Bloq", "fetching items for $id")

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(allEntries) { it.getValue<JournalEntry>(JournalEntry::class.java) }
                entriesReady()
            }
        }

        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        allEntries.clear()
        db.child("journals").child(id).child("entries").addListenerForSingleValueEvent(valueEventListener)
    }

    private fun updateImage(entry: JournalEntry) {
        fetchAllEntries(userId) { }
        if (entry.image != "") {
            val fileName = File(entry.image)
            val imageName = fileName.getName()

            var imageRef = st.child("$userId/$imageName")
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, entry.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        entry.image = it.toString()
                        db.child("journals").child(userId).child("entries").child(entry.fbId).setValue(entry)
                        Log.d("Bloq", "Updating entry image: $entry")
                    }
                }
            }
        }
    }
}