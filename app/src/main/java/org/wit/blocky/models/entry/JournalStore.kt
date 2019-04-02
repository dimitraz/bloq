package org.wit.blocky.models.entry

import org.wit.blocky.models.CalendarDate

interface JournalStore {
    fun findAll(): List<JournalEntry>
    fun findByDate(date: CalendarDate): JournalEntry?
    fun create(entry: JournalEntry)
    fun update(entry: JournalEntry)
    fun delete(entry: JournalEntry)
}