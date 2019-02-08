package org.wit.blocky.views.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.entry_fragment.*
import kotlinx.android.synthetic.main.prompt_card.view.*
import org.wit.blocky.R
import org.wit.blocky.adapters.GridAdapter
import org.wit.blocky.databinding.EntryFragmentBinding
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.JournalEntry


class EntryFragment : Fragment() {

    private lateinit var viewModel: EntryViewModel
    private lateinit var app: MainApp

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        app = activity!!.application as MainApp

        val gridAdapter = GridAdapter(context!!, app.template)
        grid.adapter = gridAdapter
//        app.template.forEachIndexed { index, s ->
//            val inflater = LayoutInflater.from(context)
//            var layout = inflater.inflate(R.layout.prompt_card, null, false)
//            layout.prompt.text = s
//
//            if (index % 2 == 0) {
//                grid.addView(layout)
//            } else {
//                grid.addView(layout)
//            }
//        }
    }

    companion object {
        fun newInstance(entry: JournalEntry) = EntryFragment().apply {
            arguments = Bundle().apply {
                putSerializable("entry", entry)
            }
        }
    }

}
