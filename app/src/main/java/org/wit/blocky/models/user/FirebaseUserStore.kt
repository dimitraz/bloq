package org.wit.blocky.models.user

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.wit.blocky.helpers.readImageFromPath
import java.io.ByteArrayOutputStream
import java.io.File

class FirebaseUserStore(val context: Context) : UserStore {

    val users = ArrayList<UserModel>()
    private lateinit var userId: String
    private lateinit var db: DatabaseReference
    private lateinit var st: StorageReference

    override fun findAll(): List<UserModel> {
        return users.filter { p -> p.authId != userId }
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
        updateImage(user)

        Log.d("Bloq", "Adding user: ${user.fbId}")
    }

    override fun update(user: UserModel) {
        Log.d("Bloq", "Updating user: ${user.fbId} $user")
        db.child("users").child(user.fbId).setValue(user)
        if (user.photoUrl.isNotEmpty() && (user.photoUrl[0] != 'h')) {
            updateImage(user)
        }
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
        st = FirebaseStorage.getInstance().reference
        users.clear()
        db.child("users").addListenerForSingleValueEvent(valueEventListener)
    }

    private fun updateImage(user: UserModel) {
        fetchUsers { }
        if (user.photoUrl != "") {
            val fileName = File(user.photoUrl)
            val imageName = fileName.name

            var imageRef = st.child("$userId/$imageName")
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, user.photoUrl)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        user.photoUrl = it.toString()
                        db.child("users").child(user.fbId).setValue(user)
                        Log.d("Bloq", "Updating user image: $user")
                    }
                }
            }
        }
    }
}