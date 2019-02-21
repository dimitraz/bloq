package org.wit.blocky.views.entry

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.bumptech.glide.request.RequestOptions.circleCropTransform
import kotlinx.android.synthetic.main.entry_fragment.*
import org.wit.blocky.R
import org.wit.blocky.adapters.PromptAdapter
import org.wit.blocky.databinding.EntryFragmentBinding
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.CalendarDate


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
        val date = bundle!!.getSerializable("date") as CalendarDate
        val binding: EntryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.entry_fragment, container, false)

        viewModel = ViewModelProviders.of(
            this, EntryViewModelFactory(activity!!.application, date)
        ).get(EntryViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app = activity!!.application as MainApp

        // Load list of prompts
        activity!!.invalidateOptionsMenu()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = PromptAdapter(app.template, false)

        // Show image
        if (viewModel.entry.image.isNotEmpty()) {
            showImage()
        }
        // Select entry image
        choose_image.setOnClickListener {
            viewModel.selectImage(this)
        }
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
                viewModel.saveEntry()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            viewModel.entry.image = data.data.toString()
            showImage()
        }
    }

    private fun showPrompts() {
        recyclerView.adapter = PromptAdapter(app.template, edit)
        activity?.invalidateOptionsMenu()
    }

    private fun showImage() {
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
