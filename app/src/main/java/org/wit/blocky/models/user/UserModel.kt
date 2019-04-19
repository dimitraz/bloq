package org.wit.blocky.models.user

import java.io.Serializable

data class UserModel(
    var fbId: String = "",
    var authId: String = "",
    var photoUrl: String = "",
    var displayName: String? = "",
    var tags: MutableList<String> = mutableListOf(),
    var email: String = "",
    var following: MutableList<String> = mutableListOf()
) : Serializable