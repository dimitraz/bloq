package org.wit.blocky.views.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.home_fragment.*
import org.wit.blocky.R
import org.wit.blocky.adapters.EntryListener
import org.wit.blocky.adapters.HomeAdapter
import org.wit.blocky.databinding.HomeFragmentBinding
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.JournalEntry


class HomeFragment : Fragment(), EntryListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var app: MainApp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: HomeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app = activity!!.application as MainApp

        val adapter = HomeAdapter(viewModel, this)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerView.adapter = adapter

        // Collapse or expand the filter view
        button.setOnClickListener {
            if (filter.isVisible) {
                collapse(filter)
            } else {
                expand(filter)
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
            "entry" to entry
        )
        Navigation.findNavController(view!!).navigate(R.id.to_entry_pager, bundle)
    }

    fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a = object : Animation() {
            protected override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            protected override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

}
