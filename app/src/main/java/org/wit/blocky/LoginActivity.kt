package org.wit.blocky

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.entry.FirebaseStore
import org.wit.blocky.models.user.FirebaseUserStore
import org.wit.blocky.models.user.UserModel

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var fireStore: FirebaseStore
    private lateinit var userStore: FirebaseUserStore
    private lateinit var auth: FirebaseAuth
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        app = application as MainApp
        fireStore = app.entries
        userStore = app.users

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        googleSignIn.setOnClickListener {
            doSignIn()
        }

        signUp.setOnClickListener {
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()
            if (email == "" || password == "") {
                showToast("Please provide email + password")
            } else {
                doSignUp(email, password)
            }
        }

        logIn.setOnClickListener {
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()
            if (email == "" || password == "") {
                showToast("Please provide email + password")
            } else {
                doLogin(email, password)
            }
        }

        progressBar.visibility = View.GONE
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                googleSignIn(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed: $e")
            }
        }
    }

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            Log.d(TAG, "signed out")
        }
    }

    private fun doSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun doLogin(email: String, password: String) {
        showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            doResult(task, auth.currentUser?.uid)
        }
    }

    private fun doSignUp(email: String, password: String) {
        showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                showToast("Sign up successful")
                doResult(task, auth.currentUser?.uid)
            } else {
                showToast("${task.exception?.message}")
            }
            hideProgress()
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    private fun googleSignIn(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                doResult(task, auth.currentUser?.uid)
            }
    }

    private fun getOrCreateUser(authId: String?) {
        if (authId != null && authId.isNotEmpty()) {
            userStore.fetchUsers {
                var user = userStore.findByAuthId(authId)
                if (user != null) {
                    Log.d(TAG, "Found user: $user")
                } else {
                    Log.d(TAG, "No user found, creating user")

                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.let {
                        user = UserModel(
                            photoUrl = currentUser.photoUrl.toString(),
                            displayName = currentUser.displayName,
                            email = currentUser.email!!,
                            authId = authId
                        )
                        userStore.create(user!!)
                    }
                }
                app.currentUser = user!!
            }
        }
    }

    private fun doResult(task: Task<AuthResult>, authId: String?) {
        if (task.isSuccessful) {
            getOrCreateUser(authId)

            showToast("Login successful")
            fireStore.fetchEntries {
                hideProgress()
                startActivity(Intent(this, MainActivity::class.java))
            }
        } else {
            showToast("${task.exception?.message}")
        }
        hideProgress()
    }

    companion object {
        private const val TAG = "Bloq"
        private const val RC_SIGN_IN = 9001
    }
}
