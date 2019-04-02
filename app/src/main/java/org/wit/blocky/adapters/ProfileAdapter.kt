package org.wit.blocky.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.card_follow.view.*
import org.wit.blocky.R
import org.wit.blocky.models.entry.JournalEntry
import org.wit.blocky.views.profile.ProfileViewModel

class ProfileAdapter(
    private val viewModel: ProfileViewModel,
    private val listener: EntryListener
) :
    RecyclerView.Adapter<ProfileAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.card_follow, parent, false),
            viewModel.results.value!!
        )
    }

    override fun getItemCount(): Int {
        val size = viewModel.results.value?.size
        return if (size != null) size!! else {
            0
        }
    }

    override fun onBindViewHolder(holder: ProfileAdapter.MainHolder, position: Int) {
        val entry = viewModel.results.value!![holder.adapterPosition]
        holder.bind(entry, listener)
    }

    class MainHolder constructor(
        itemView: View,
        private val entries: List<JournalEntry>
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(entry: JournalEntry, listener: EntryListener) {
            itemView.entry_date.text = entry.date.toString()
            itemView.entry_author.text = entry.authorName
            itemView.entry_category.setText(entry.category)
            itemView.setOnClickListener {
                listener.onEntryClick(entries.indexOf(entry), entry)
            }
            if (entry.image.isNotEmpty()) {
                itemView.entry_image.visibility = View.VISIBLE
                Glide
                    .with(itemView.context)
                    .load(entry.image)
                    .into(itemView.entry_image)
            } else {
                itemView.entry_image.visibility = View.GONE
            }
        }
    }
}