package org.wit.blocky.views.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_home.*
import org.wit.blocky.R
import org.wit.blocky.adapters.EntryListener
import org.wit.blocky.adapters.HomeAdapter
import org.wit.blocky.databinding.FragmentHomeBinding
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.entry.JournalEntry

class HomeFragment : Fragment(), EntryListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var app: MainApp
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        app = activity!!.application as MainApp
        recyclerView = binding.root.findViewById(R.id.recyclerView)
        progressBar = binding.root.findViewById(R.id.progressBar)

        // Show progress bar
        showProgress()

        // Fetch entries from firebase
        app.entries.fetchEntries {
            Log.d("Bloq", "Fetching entries")

            // Load view model
            viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
            viewModel.entries = app.entries.findAll().toMutableList()
            binding.viewModel = viewModel

            // Load recycler view
            adapter = HomeAdapter(viewModel, this)
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)
            recyclerView.adapter = adapter
            hideProgress()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Collapse or expand the filter view
        button.setOnClickListener {
            if (filter.isVisible) {
                viewModel.collapse(filter)
            } else {
                viewModel.expand(filter)
            }
        }

        // Add a chip to the chipGroup for each available category
        for (item in app.categories) {
            val chip = Chip(chipGroup.context)
            chip.text = "$item"
            chip.isClickable = true
            chip.isCheckable = true
            chipGroup.addView(chip)
        }

        // Chip listeners
        for (item in chipGroup.children) {
            (item as Chip).setOnCheckedChangeListener { _, _ ->
                val category = item.text.toString()
                if (item.isChecked) {
                    // Add the chip to the list of categories to filter by
                    viewModel.categories.add(category)
                } else {
                    if (viewModel.categories.contains(category)) {
                        // The chip is now deselected, so remove it from
                        // the list of categories to filter by
                        viewModel.categories.remove(category)
                    }
                }

                // Filter by selected categories
                adapter.filter.filter(searchView.query)
            }
        }

        // Search query listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                adapter.filter.filter(query)
                return false
            }
        })
    }

    // Add listener for when an entry card is pressed
    override fun onEntryClick(position: Int, entry: JournalEntry) {
        val bundle = bundleOf(
            "position" to position,
            "date" to entry.date
        )
        Navigation.findNavController(view!!).navigate(R.id.to_destination_entry_pager, bundle)
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.menu_toolbar, menu)
    }
}
