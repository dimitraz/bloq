package org.wit.blocky.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.home_card.view.*
import org.wit.blocky.R
import org.wit.blocky.models.JournalEntry
import org.wit.blocky.views.home.HomeViewModel

class HomeAdapter(
    private val viewModel: HomeViewModel,
    private val listener: EntryListener
) :
    RecyclerView.Adapter<HomeAdapter.MainHolder>(), Filterable {

//    val entries = viewModel.entries

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.home_card, parent, false), viewModel.entries)
    }

    override fun getItemCount(): Int = viewModel.entries.size

    override fun onBindViewHolder(holder: HomeAdapter.MainHolder, position: Int) {
        val entry = viewModel.entries[holder.adapterPosition]
        holder.bind(entry, listener)
    }

    override fun getFilter(): Filter {
        return searchFilter
    }

    private val searchFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val filteredList: MutableList<JournalEntry> = ArrayList()

            if (constraint == null || constraint.isEmpty()) {
                for (item in viewModel.allEntries) {
                    if (viewModel.categories.isNotEmpty() && item.category in viewModel.categories) {
                        filteredList.add(item)
                    } else if (viewModel.categories.isEmpty()) {
                        filteredList.add(item)
                    }
                }
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }

                for (item in viewModel.allEntries) {
                    if (item.notes!!.toLowerCase().contains(filterPattern)) {
                        if (viewModel.categories.isNotEmpty() && item.category in viewModel.categories) {
                            filteredList.add(item)
                        } else if (viewModel.categories.isEmpty()) {
                            filteredList.add(item)
                        }
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

    fun filterCategories(item: JournalEntry, filteredList: MutableList<JournalEntry>) {
        if (viewModel.categories.isNotEmpty() && item.category in viewModel.categories) {
            filteredList.add(item)
        } else if (viewModel.categories.isEmpty()) {
            filteredList.add(item)
        }
    }

    class MainHolder constructor(
        itemView: View,
        private val entries: List<JournalEntry>
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(entry: JournalEntry, listener: EntryListener) {
            itemView.entry_date.text = entry.date.toString()
            itemView.entry_category.setText(entry.category)
            itemView.entry_favourite.isChecked = entry.bookmarked
            itemView.setOnClickListener {
                listener.onEntryClick(entries.indexOf(entry), entry)
            }
            if (entry.image.isNotEmpty()) {
                itemView.imageView.visibility = View.VISIBLE
                Glide
                    .with(itemView.context)
                    .load(entry.image)
                    .into(itemView.imageView)
            } else {
                itemView.imageView.visibility = View.GONE
            }
        }
    }
}

interface EntryListener {
    fun onEntryClick(position: Int, entry: JournalEntry)
}