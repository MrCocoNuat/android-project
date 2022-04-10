package com.example.pcpresenter

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.parse.ParseUser
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.Charset

class LoginFragment(private val onLoginSuccessListener : OnLoginSuccessListener) : Fragment() {

    interface OnLoginSuccessListener{
        fun onLoginSuccess(username : String, password : String, remember : Boolean)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i(TAG,"Login Fragment opened")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val etUsername = view.findViewById<EditText>(R.id.etUsername)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val buttonSignIn = view.findViewById<Button>(R.id.buttonSignIn)
        val buttonSignUp = view.findViewById<Button>(R.id.buttonSignUp)
        val checkboxRemember = view.findViewById<CheckBox>(R.id.checkboxRemember)
        val etErrorText = view.findViewById<TextView>(R.id.tvErrorText)
        etErrorText.text = ""

        buttonSignIn.setOnClickListener{
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            //try a login
            ParseUser.logInInBackground(username,password){ user, _ ->
                if (user != null){
                    // good! save credentials to a cache and finish
                    onLoginSuccessListener.onLoginSuccess(username, password, checkboxRemember.isChecked)
                }
                else{
                    // no good
                    etErrorText.text = "Incorrect credentials"
                }
            }
        }
        buttonSignUp.setOnClickListener{
            val newUser = ParseUser()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            newUser.username = username
            newUser.setPassword(password)

            //try a sign up
            newUser.signUpInBackground { e ->
                if (e == null){
                    // good! save credentials to a cache and finish
                    onLoginSuccessListener.onLoginSuccess(username, password, checkboxRemember.isChecked)
                }
                else{
                    // no good
                    etErrorText.text = "Sign up failed. Duplicate usernames?"
                }
            }
        }

        return view
    }

    companion object{
        private const val TAG = "LoginFragment"
    }
}
