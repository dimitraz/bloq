package org.wit.blocky.views.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_profile.*
import org.wit.blocky.R
import org.wit.blocky.adapters.EntryListener
import org.wit.blocky.adapters.ProfileAdapter
import org.wit.blocky.databinding.FragmentProfileBinding
import org.wit.blocky.helpers.imageIntent
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.entry.FirebaseStore
import org.wit.blocky.models.entry.JournalEntry
import org.wit.blocky.models.user.UserModel

class ProfileFragment : Fragment(), EntryListener {

    private lateinit var app: MainApp
    private lateinit var viewModel: ProfileViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProfileAdapter
    private lateinit var user: UserModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        app = activity!!.application as MainApp
        user = app.currentUser
        val bundle = arguments
        if (bundle != null) {
            user = bundle.getSerializable("user") as UserModel
        }

        // Data binding
        viewModel = ViewModelProviders.of(
            this, ProfileViewModelFactory(activity!!.application)
        ).get(ProfileViewModel::class.java)
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

        val fireStore = FirebaseStore(context!!)
        if (user == app.currentUser) {
            // Set profile image listeners
            profile_image.setOnClickListener {
                startActivityForResult(imageIntent(), 2)
            }
            choose_image.setOnClickListener {
                startActivityForResult(imageIntent(), 2)
            }
            choose_image.visibility = View.VISIBLE
            profile_name.visibility = View.INVISIBLE
            profile_name_edit.visibility = View.VISIBLE

            viewModel.displayName.observe(this, Observer {
                app.currentUser.displayName = viewModel.displayName.value
                app.users.update(app.currentUser)
            })

            val following = user.following
            if (following.isEmpty()) {
                following_text.text = "You are not currently following any users"
                hideProgress()
            } else {
                Log.i("Bloq", "item $following")
                for (item in following) {
                    showProgress()
                    Log.i("Bloq", "item $item")
                    fireStore.fetchAllEntries(item) {
                        viewModel.addAll(fireStore.allEntries)
                        adapter.notifyDataSetChanged()
                        hideProgress()
                    }
                }
            }
        } else {
            showProgress()
            following_header.setText(R.string.header_entries)
            fireStore.fetchAllEntries(user.authId) {
                viewModel.addAll(fireStore.allEntries)
                adapter.notifyDataSetChanged()
                hideProgress()
            }
        }

        // Name, email address, and profile photo Url
        if (user.displayName?.isNotEmpty()!!) {
            profile_name.text = user.displayName
        } else {
            profile_name.setText(R.string.default_display_name)
        }
        profile_email.text = user.email

        if (user.photoUrl.isNotEmpty()) {
            Glide
                .with(this)
                .load(user.photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(profile_image)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data.toString()

        Glide
            .with(this)
            .load(uri)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_image)

        user.photoUrl = uri
        app.users.update(user)
    }

    // Add listener for when an entry card is pressed
    override fun onEntryClick(position: Int, entry: JournalEntry) {
        Log.i("Bloq", "Entry: $entry")

        val bundle = bundleOf(
            "entry" to entry
        )
        Navigation.findNavController(view!!).navigate(R.id.destination_entry, bundle)
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    companion object {
        fun newInstance(user: UserModel) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putSerializable("user", user)
            }
        }
    }
}
