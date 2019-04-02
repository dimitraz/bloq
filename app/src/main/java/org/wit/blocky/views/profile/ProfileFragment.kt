package org.wit.blocky.views.profile

import android.content.Intent
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_profile.*
import org.wit.blocky.R
import org.wit.blocky.adapters.EntryListener
import org.wit.blocky.adapters.ProfileAdapter
import org.wit.blocky.databinding.FragmentProfileBinding
import org.wit.blocky.helpers.imageIntent
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
    private lateinit var user: FirebaseUser

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

        // Name, email address, and profile photo Url
        profile_name.text = app.currentUser.displayName
        profile_email.text = app.currentUser.email

        if (app.currentUser.photoUrl.isNotEmpty()) {
            Glide
                .with(this)
                .load(app.currentUser.photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(profile_image)
        }

        profile_image.setOnClickListener {
            startActivityForResult(imageIntent(), 2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data.toString()
        viewModel.image = uri

        Log.i("Bloq", "$uri, ${viewModel.image}")

        Glide
            .with(this)
            .load(viewModel.image)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_image)

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName("Jane Q. User")
            .setPhotoUri(Uri.parse(uri))
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("Bloq", "User profile updated.")
                }
            }
    }

    // Add listener for when an entry card is pressed
    override fun onEntryClick(position: Int, entry: JournalEntry) {
        Log.i("Bloq", "Entry: $entry")
    }
}
