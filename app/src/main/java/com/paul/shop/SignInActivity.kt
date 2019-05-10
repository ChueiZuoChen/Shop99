package com.paul.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signin.setOnClickListener {
            signUp()
        }
        login.setOnClickListener {
            logIn()
        }
    }

    private fun logIn() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email_auth.text.toString(), password.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    AlertDialog.Builder(this@SignInActivity)
                        .setTitle("Login")
                        .setPositiveButton("OK", null)
                        .setMessage("${it.exception?.message}")
                        .show()
                }
            }
    }

    private fun signUp() {
        val sEmail = email_auth.text.toString()
        val sPassword = password.text.toString()
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(sEmail, sPassword)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    AlertDialog.Builder(this@SignInActivity)
                        .setTitle("Sign UP")
                        .setMessage("Account created.")
                        .setPositiveButton("OK") { dialog, which ->
                            setResult(Activity.RESULT_OK)
                            finish()
                        }.show()
                } else {
                    AlertDialog.Builder(this@SignInActivity)
                        .setTitle("Sign UP")
                        .setPositiveButton("OK", null)
                        .setMessage("${it.exception?.message}")
                        .show()
                }
            }
    }
}
