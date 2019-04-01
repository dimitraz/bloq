package org.wit.blocky.models.user

import java.io.Serializable

data class UserModel(
    var fbId: String = "",
    var photoUrl: String = "",
    var displayName: String = "",
    var email: String = "",
    var following: List<String> = listOf()
) : Serializable