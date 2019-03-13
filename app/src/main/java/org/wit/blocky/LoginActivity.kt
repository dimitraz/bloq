package org.wit.blocky

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.wit.blocky.main.MainApp
import org.wit.blocky.models.store.FirebaseStore

class LoginActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var fireStore: FirebaseStore
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp

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

    private fun doLogin(email: String, password: String) {
        showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                showToast("Login successful")
                fireStore.fetchEntries {
                    hideProgress()
                    //startActivity(Intent(this, MainActivity::class.java))
                }
            } else {
                showToast("${task.exception?.message}")
            }
            hideProgress()
        }

    }

    private fun doSignUp(email: String, password: String) {
        showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                showToast("Sign up successful")
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
}
