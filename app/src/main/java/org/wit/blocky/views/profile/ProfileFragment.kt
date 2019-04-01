package org.wit.blocky.views.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import kotlinx.android.synthetic.main.fragment_profile.*
import org.wit.blocky.R
import org.wit.blocky.adapters.EntryListener
import org.wit.blocky.adapters.ProfileAdapter
import org.wit.blocky.databinding.FragmentProfileBinding
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.entry.FirebaseStore
import org.wit.blocky.models.entry.JournalEntry

class ProfileFragment : Fragment(), EntryListener {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var app: MainApp
    private lateinit var viewModel: ProfileViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        // Data binding
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        // Load recycler view
        adapter = ProfileAdapter(viewModel, this)
        recyclerView = binding.root.findViewById(R.id.following_list)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, 1)
        recyclerView.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app = activity!!.application as MainApp

        val following = app.currentUser.following
        if (following.isEmpty()) {
            following_text.text = "You are not currently following any users"
        } else {
            val fireStore = FirebaseStore(context!!)
            for (item in following) {
                fireStore.fetchAllEntries(item) {
                    viewModel.addAll(fireStore.allEntries)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    // Add listener for when an entry card is pressed
    override fun onEntryClick(position: Int, entry: JournalEntry) {
        Log.i("Bloq", "Entry: $entry")
    }
}
