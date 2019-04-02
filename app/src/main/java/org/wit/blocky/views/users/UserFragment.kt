package org.wit.blocky.views.users

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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_users.*
import org.wit.blocky.R
import org.wit.blocky.adapters.UserAdapter
import org.wit.blocky.adapters.UserListener
import org.wit.blocky.databinding.FragmentUsersBinding
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.CalendarDate
import org.wit.blocky.models.user.UserModel
import org.wit.blocky.views.entry.EntryFragment
import org.wit.blocky.views.profile.ProfileFragment

class UserFragment : Fragment(), UserListener {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var app: MainApp
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUsersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false)

        // Data binding
        viewModel = ViewModelProviders.of(
            this, UserViewModelFactory(activity!!.application)
        ).get(UserViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app = activity!!.application as MainApp

        // Load list of prompts
        adapter = UserAdapter(viewModel, this, app)
        user_list.layoutManager = LinearLayoutManager(activity)
        user_list.adapter = adapter

        viewModel.users.observe(this, Observer {
            adapter.notifyDataSetChanged()
        })
    }

    override fun onUserClick(user: UserModel) {
        Log.i("Bloq", "User clicked: $user")

        val bundle = bundleOf(
            "user" to user
        )
        Navigation.findNavController(view!!).navigate(R.id.destination_profile, bundle)
    }
}
