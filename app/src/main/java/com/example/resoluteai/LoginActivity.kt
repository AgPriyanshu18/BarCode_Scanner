package com.example.resoluteai

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.resoluteai.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    var binding : ActivityLoginBinding?= null
    lateinit var pDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSignIn?.setOnClickListener{
            signInRegistered()
        }
        pDialog = ProgressDialog(this)
    }


    fun signInSuccess(){
        pDialog.dismiss()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun signInRegistered(){
        val email : String = binding?.etEmail?.text.toString().trim{it<=' '}
        val password : String = binding?.etPass?.text.toString().trim{it<=' '}
        if (validateForm(email,password)){
            pDialog.setMessage("Please Wait..")
            pDialog.show()
            val auth : FirebaseAuth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    pDialog.dismiss()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Sign In", "createUserWithEmail:success")
                        signInSuccess()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("Sign In", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateForm(email : String , password  :String ): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Please enter the email address", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }
}