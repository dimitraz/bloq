package org.wit.blocky.views.users

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.user.UserModel

class UserViewModel(application: Application) : ViewModel() {
    var app = application as MainApp
    var users = MutableLiveData<List<UserModel>>()

    init {
        app.users.fetchUsers {
            Log.d("Bloq", "From user view model: ${app.users.findAll()}")
            users.value = app.users.findAll()
        }
    }
}

class UserViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(application) as T
    }
}