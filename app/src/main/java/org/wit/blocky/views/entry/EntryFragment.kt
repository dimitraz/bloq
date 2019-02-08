package org.wit.blocky.views.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.wit.blocky.R
import org.wit.blocky.databinding.EntryFragmentBinding
import org.wit.blocky.models.JournalEntry

class EntryFragment : Fragment() {

    private lateinit var viewModel: EntryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        val entry = bundle!!.getSerializable("entry") as JournalEntry
        val binding: EntryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.entry_fragment, container, false)

        viewModel = ViewModelProviders.of(
            this, EntryViewModelFactory(entry)
        ).get(EntryViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    companion object {
        fun newInstance(entry: JournalEntry) = EntryFragment().apply {
            arguments = Bundle().apply {
                putSerializable("entry", entry)
            }
        }
    }

}
