package org.wit.blocky.models.user

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseUserStore(val context: Context) : UserStore {

    val users = ArrayList<UserModel>()
    private lateinit var db: DatabaseReference
    private lateinit var userId: String

    override fun findAll(): ArrayList<UserModel> {
        return users
    }

    override fun findByAuthId(authId: String): UserModel? {
        return users.find { p -> p.authId == authId }
    }

    override fun findByFbId(fbId: String): UserModel? {
        return users.find { p -> p.fbId == fbId }
    }

    override fun create(user: UserModel) {
        Log.d("Bloq", "Adding user: $user")

        val key = db.child("users").push().key
        key?.let {
            user.fbId = key
            users.add(user)
            db.child("users").child(key).setValue(user)
        }
        Log.d("Bloq", "Adding user: ${user.fbId}")
    }

    override fun update(user: UserModel) {
        Log.d("Bloq", "Updating user: ${user.fbId} $user")
        db.child("users").child(user.fbId).setValue(user)
    }

    override fun delete(user: UserModel) {
        Log.d("Bloq", "Deleting user: ${user.fbId}")
        db.child("users").child(user.fbId).removeValue()
        users.remove(user)
    }

    fun fetchUsers(usersReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(users) { it.getValue<UserModel>(UserModel::class.java) }
                usersReady()
            }
        }

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        users.clear()
        db.child("users").addListenerForSingleValueEvent(valueEventListener)
    }
}