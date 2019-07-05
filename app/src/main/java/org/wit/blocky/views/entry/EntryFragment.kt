package org.wit.blocky.views.entry

import android.R.layout.simple_spinner_dropdown_item
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import kotlinx.android.synthetic.main.fragment_entry.*
import org.wit.blocky.R
import org.wit.blocky.adapters.PromptAdapter
import org.wit.blocky.databinding.FragmentEntryBinding
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

        val bundle = arguments
        val binding: FragmentEntryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_entry, container, false)

        viewModel = ViewModelProviders.of(
            this, EntryViewModelFactory(activity!!.application, bundle)
        ).get(EntryViewModel::class.java)
        binding.setLifecycleOwner(this)
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

        entry_author.text = viewModel.entry!!.authorName

        // Show image
        if (viewModel.entry!!.image.isNotEmpty()) {
            showImage()
        } else {
            entry_image.visibility = View.GONE
        }

        if (app.currentUser.authId != viewModel.entry!!.authorId) {
            choose_image.visibility = View.GONE
            category.visibility = View.GONE
            checkBox.visibility = View.GONE
            secondaryToolbar.visibility = View.GONE
        } else {
            // Select entry image
            choose_image.setOnClickListener {
                viewModel.selectImage(this)
            }

            var selectedItem = -1
            val items = app.currentUser.tags
            val adapter = object : ArrayAdapter<String>(activity, simple_spinner_dropdown_item, items) {
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    var v: View? = null
                    v = super.getDropDownView(position, null, parent)
                    // If this is the selected item position
                    if (position == selectedItem) {
                        v!!.setBackgroundColor(Color.LTGRAY)
                    } else {
                        // for other views
                        v!!.setBackgroundColor(Color.WHITE)
                    }
                    return v
                }
            }
            category.adapter = adapter

            if (viewModel.entry!!.category.isNotEmpty()) {
                val pos = adapter.getPosition(viewModel.entry!!.category)
                category.setSelection(pos)
            }

            category.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val tag = parent.getItemAtPosition(position) as String
                    Log.v("Bloq", "Selected tag: $tag")
                    selectedItem = position
                    viewModel.entry!!.category = tag
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            secondaryToolbar.inflateMenu(R.menu.menu_entry)
            secondaryToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_save -> {
                        viewModel.saveEntry()
                        nav.navigate(R.id.destination_home)
                    }
                    R.id.item_delete -> {
                        showDialog()
                    }
                }
                false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            viewModel.entry!!.image = data.data.toString()
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
            .load(viewModel.entry!!.image)
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
