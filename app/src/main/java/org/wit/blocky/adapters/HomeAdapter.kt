package org.wit.blocky.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.home_card.view.*
import org.wit.blocky.R
import org.wit.blocky.models.JournalEntry

class HomeAdapter(private var entries: List<JournalEntry>) : RecyclerView.Adapter<HomeAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.home_card, parent, false))
    }

    override fun getItemCount(): Int = entries.size

    override fun onBindViewHolder(holder: HomeAdapter.MainHolder, position: Int) {
        val entry = entries[holder.adapterPosition]
        holder.bind(entry)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(entry: JournalEntry) {
            itemView.entry_title.text = entry.title
            itemView.entry_notes.text = entry.notes
            itemView.entry_favourite.isChecked = entry.bookmarked
        }
    }

}
