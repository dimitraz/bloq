package org.wit.blocky.views.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.wit.blocky.R
import org.wit.blocky.databinding.FragmentUsersBinding
import org.wit.blocky.main.MainApp

class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var app: MainApp
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUsersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false)

        // Data binding
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app = activity!!.application as MainApp
    }
}
