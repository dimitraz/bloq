package org.wit.blocky.views.entry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.fragment_entry.*
import org.wit.blocky.R
import org.wit.blocky.adapters.PromptAdapter
import org.wit.blocky.databinding.EntryFragmentBinding
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.CalendarDate

class EntryFragment : Fragment() {

    private lateinit var viewModel: EntryViewModel
    private lateinit var app: MainApp
    private lateinit var nav: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        var date = CalendarDate(CalendarDay.today().day, CalendarDay.today().month, CalendarDay.today().year)
        val bundle = arguments
        if (bundle != null) {
            date = bundle.getSerializable("date") as CalendarDate
        }

        val binding: EntryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_entry, container, false)

        viewModel = ViewModelProviders.of(
            this, EntryViewModelFactory(activity!!.application, date)
        ).get(EntryViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app = activity!!.application as MainApp

        // Navigation controller
        nav = Navigation.findNavController(view!!)

        // Load list of prompts
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = PromptAdapter(app.template, viewModel)

        // Show image
        if (viewModel.entry.image.isNotEmpty()) {
            showImage()
        } else {
            entry_image.visibility = View.GONE
        }

        // Select entry image
        choose_image.setOnClickListener {
            viewModel.selectImage(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_save -> {
                viewModel.saveEntry()
                nav.navigate(R.id.destination_home)
            }
            R.id.item_delete -> {
                showDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.entry_menu, menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            viewModel.entry.image = data.data.toString()
            showImage()
        }
    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(context!!)
            .setTitle("Delete entry")
            .setMessage("Are you sure you want to delete this entry?")
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel, null)
            .show()

        val ok = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        ok.setOnClickListener {
            viewModel.deleteEntry()
            nav.navigate(R.id.destination_home)
            alertDialog.dismiss()
        }
    }

    private fun showImage() {
        entry_image.visibility = View.VISIBLE
        Glide
            .with(this)
            .load(viewModel.entry.image)
            .apply(centerCropTransform())
            .into(entry_image)
    }

    companion object {
        fun newInstance(date: CalendarDate) = EntryFragment().apply {
            arguments = Bundle().apply {
                putSerializable("date", date)
            }
        }
    }
}
