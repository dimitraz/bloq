package org.wit.blocky.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.wit.blocky.models.JournalEntry
import org.wit.blocky.views.entry.EntryFragment

class PagerAdapter(fragmentManager: FragmentManager, private val entries: MutableList<JournalEntry>) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        val fragment = EntryFragment.newInstance(entries[position])
        fragment.arguments = bundleOf(
            "entry" to entries[position]
        )
        return fragment
    }

    override fun getCount(): Int = entries.size

}