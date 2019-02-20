package org.wit.blocky.views.entry

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.entry_fragment.*
import org.wit.blocky.R
import org.wit.blocky.adapters.PromptAdapter
import org.wit.blocky.databinding.EntryFragmentBinding
import org.wit.blocky.helpers.CalendarHelpers
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.JournalEntry


class EntryFragment : Fragment() {

    private lateinit var viewModel: EntryViewModel
    private lateinit var app: MainApp
    private var edit: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val bundle = arguments
        val entry = bundle!!.getSerializable("entry") as JournalEntry
        val binding: EntryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.entry_fragment, container, false)

        viewModel = ViewModelProviders.of(
            this, EntryViewModelFactory(entry)
        ).get(EntryViewModel::class.java)
        binding.viewModel = viewModel
        binding.helpers = CalendarHelpers()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app = activity!!.application as MainApp

        activity!!.invalidateOptionsMenu()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = PromptAdapter(app.template, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.item_edit).isVisible = !edit
        menu.findItem(R.id.item_save).isVisible = edit
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_edit -> {
                edit = true
                showPrompts()
            }
            R.id.item_save -> {
                edit = false
                showPrompts()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showPrompts() {
        recyclerView.adapter = PromptAdapter(app.template, edit)
        activity?.invalidateOptionsMenu()
    }

    companion object {
        fun newInstance(entry: JournalEntry) = EntryFragment().apply {
            arguments = Bundle().apply {
                putSerializable("entry", entry)
            }
        }
    }

}
