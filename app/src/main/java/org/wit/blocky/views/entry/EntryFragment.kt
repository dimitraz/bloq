package org.wit.blocky.views.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.wit.blocky.R
import org.wit.blocky.models.JournalEntry

class EntryFragment : Fragment() {

    private lateinit var viewModel: EntryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.entry_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EntryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {
        fun newInstance(entry: JournalEntry) = EntryFragment().apply {
            arguments = Bundle().apply {
                putSerializable("entry", entry)
            }
        }
    }

}
