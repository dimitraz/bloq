package org.wit.blocky.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_card.view.*
import org.wit.blocky.R
import org.wit.blocky.helpers.CalendarHelpers
import org.wit.blocky.models.JournalEntry
import org.wit.blocky.views.home.HomeViewModel

class HomeAdapter(
    private val viewModel: HomeViewModel,
    private val listener: EntryListener
) :
    RecyclerView.Adapter<HomeAdapter.MainHolder>(), Filterable {
    val entries = viewModel.entries

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.home_card, parent, false), entries)
    }

    override fun getItemCount(): Int = entries.size

    override fun onBindViewHolder(holder: HomeAdapter.MainHolder, position: Int) {
        val entry = entries[holder.adapterPosition]
        holder.bind(entry, listener)
    }

    override fun getFilter(): Filter {
        return searchFilter
    }

    private val searchFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val filteredList: MutableList<JournalEntry> = ArrayList()

            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(viewModel.allEntries)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }

                for (item in viewModel.allEntries) {
                    if (item.notes!!.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            val results = Filter.FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            viewModel.entries.clear()
            viewModel.entries.addAll(results.values as List<JournalEntry>)
            notifyDataSetChanged()
        }
    }

    class MainHolder constructor(
        itemView: View,
        private val entries: List<JournalEntry>
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(entry: JournalEntry, listener: EntryListener) {
            itemView.entry_date.text = CalendarHelpers().dateToString(entry.date!!)
            itemView.entry_category.text = "Lifestyle"
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