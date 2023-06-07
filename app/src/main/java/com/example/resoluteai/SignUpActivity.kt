package com.example.resoluteai

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.example.resoluteai.databinding.ActivityLoginBinding
import com.example.resoluteai.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class SignUpActivity : AppCompatActivity() {
    var binding : ActivitySignUpBinding ?= null
    lateinit var pDialog : ProgressDialog
    private  val mFireStore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        pDialog = ProgressDialog(this)

        binding?.buttonSubmitSignup?.setOnClickListener{
            registerUser()
        }
    }

    fun userRegisteredSuccess(){
        Toast.makeText(this,"You have registered",Toast.LENGTH_SHORT).show()
        pDialog.dismiss()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun registerUser(){
        val name : String  = binding?.formIpName?.text.toString().trim{it <= ' '}
        val email : String = binding?.formIpEmail?.text.toString().trim{it<=' '}
        val password : String = binding?.formIpPass?.text.toString().trim{it<=' '}

        if (validateForm(name,email,password)){
            pDialog.setMessage("Please wait..")
            pDialog.show()
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userRegisteredSuccess()
                    } else {
                        pDialog.dismiss()
                        Toast.makeText(this,
                            task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    private fun validateForm(name :String , email : String , password  :String ): Boolean {
        return when{
            TextUtils.isEmpty(name) ->{
                Toast.makeText(this, "Please enter the name", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(email) ->{
                Toast.makeText(this, "Please enter the email address", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(password) ->{
                Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show()
                false
            }else->{
                true
            }
        }
    }

}