package org.wit.blocky.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_card.view.*
import org.wit.blocky.R
import org.wit.blocky.helpers.CalendarHelpers
import org.wit.blocky.models.JournalEntry

class HomeAdapter(
    private val entries: List<JournalEntry>,
    private val listener: EntryListener
) :
    RecyclerView.Adapter<HomeAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.home_card, parent, false), entries)
    }

    override fun getItemCount(): Int = entries.size

    override fun onBindViewHolder(holder: HomeAdapter.MainHolder, position: Int) {
        val entry = entries[holder.adapterPosition]
        holder.bind(entry, listener)
    }

    class MainHolder constructor(
        itemView: View,
        private val entries: List<JournalEntry>
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(entry: JournalEntry, listener: EntryListener) {
            itemView.entry_date.text = CalendarHelpers().dateToString(entry.date!!)
            itemView.entry_notes.text = entry.notes
            itemView.entry_favourite.isChecked = entry.bookmarked
            itemView.setOnClickListener {
                listener.onEntryClick(entries.indexOf(entry), entry)
            }
            if (entry.image.isNotEmpty()) {
                itemView.imageView.viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // Only call once
                        itemView.imageView.viewTreeObserver
                            .removeOnGlobalLayoutListener(this)

                        // Load image into image view
                        Picasso.get()
                            .load(R.drawable.image)
                            .resize(itemView.imageView.measuredWidth, 0)
                            .centerCrop().into(itemView.imageView)
                    }
                })
            }
        }
    }

}

interface EntryListener {
    fun onEntryClick(position: Int, entry: JournalEntry)
}