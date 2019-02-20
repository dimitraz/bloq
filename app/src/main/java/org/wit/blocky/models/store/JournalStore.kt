package org.wit.blocky.models.store

import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.JournalEntry

interface JournalStore {
    fun findAll(): List<JournalEntry>
    fun findByDate(date: CalendarDate): JournalEntry?
    fun create(entry: JournalEntry)
    fun update(entry: JournalEntry)
    fun delete(entry: JournalEntry)
}