package org.wit.blocky.views.tags

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_tags.*
import org.wit.blocky.R
import org.wit.blocky.adapters.ProfileAdapter
import org.wit.blocky.databinding.FragmentTagsBinding
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.entry.FirebaseStore
import org.wit.blocky.models.user.UserModel

class TagFragment : Fragment() {

    private lateinit var app: MainApp
    private lateinit var viewModel: TagViewModel
    private lateinit var user: UserModel
    private lateinit var fireStore: FirebaseStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTagsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tags, container, false)

        app = activity!!.application as MainApp
        user = app.currentUser

        // Data binding
        viewModel = ViewModelProviders.of(
            this, TagViewModelFactory(activity!!.application)
        ).get(TagViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fireStore = FirebaseStore(context!!)

        addTag.setOnClickListener {
            viewModel.addTag(addTagField.text.toString())
            addTagField.setText("")
        }

        viewModel.tags.observe(this, Observer {
            // Add a chip to the chipGroup for each available category
            tagGroup.removeAllViewsInLayout()

            for (item in app.currentUser.tags) {
                val chip = Chip(tagGroup.context)
                chip.text = "$item"
                chip.isClickable = true
                chip.isCheckable = true
                chip.isChecked = true
                tagGroup.addView(chip)

                chip.setOnClickListener {
                    if (!chip.isChecked) {
                        showDialog(chip)
                    }
                }
            }
        })
    }

    private fun showDialog(chip: Chip) {
        val tag = chip.text.toString()
        val alertDialog = AlertDialog.Builder(context!!)
            .setTitle("Delete tag: $tag")
            .setMessage("Are you sure you want to delete this tag from all entries?")
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel, null)
            .show()

        val ok = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        ok.setOnClickListener {
            Log.i("Bloq", "Deleting tag: $tag")
            viewModel.deleteTag(tag, fireStore)
            alertDialog.dismiss()
        }

        val cancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        cancel.setOnClickListener {
            chip.isChecked = true
            alertDialog.dismiss()
        }
    }

}
