package org.wit.blocky.models

import com.prolificinteractive.materialcalendarview.CalendarDay

interface JournalStore {
    fun findAll(): List<JournalEntry>
    fun findByDate(date: CalendarDay) : JournalEntry?
    fun create(entry: JournalEntry)
    fun update(entry: JournalEntry)
    fun delete(entry: JournalEntry)
}