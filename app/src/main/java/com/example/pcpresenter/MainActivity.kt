package com.example.pcpresenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseUser
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private lateinit var actionBar : ActionBar

    private lateinit var currentFragment : Fragment
    private lateinit var feedFragment: Fragment
    private lateinit var createFragment: Fragment

    private lateinit var openDetailFragmentNotifier: RigFeedFragment.OpenDetailFragmentNotifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar = supportActionBar!!

        openDetailFragmentNotifier = object : RigFeedFragment.OpenDetailFragmentNotifier{
            override fun openDetailFragment(rig: Rig) {
                openDetail(rig)
            }
        }

        feedFragment = RigFeedFragment(null, openDetailFragmentNotifier)
        createFragment = CreateFragment()
        currentFragment = feedFragment

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            Log.i(TAG,"Bottom clicked")
            when (item.itemId) {
                R.id.action_feed -> { openFeed(); true }
                R.id.action_create -> { openCapture(); true }
                R.id.action_profile -> { openProfile();true }
                else -> true
            }
        }

        // check for presence of logged-in user in file
        val credentials = loadCredentials()
        //login now
        if (credentials == null) {
            Log.i(TAG, "No cached user")
            openFeed()
        } else ParseUser.logInInBackground(credentials[0], credentials[1]) { user, _ ->
            if (user != null) Log.i(TAG, "Auto-login as ${user.username} success!")
            else Log.e(TAG, "Auto-login failed!")
            openFeed()
        }

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(R.id.placeholder,feedFragment)
            .commit()

    }


    fun openFeed(){
        Log.i(TAG,"Opening Feed fragment")
        actionBar.title = "Feed"
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            //.hide(currentFragment)
            .replace(R.id.placeholder,feedFragment)
            .commit()
        currentFragment = feedFragment
    }

    fun openDetail(rig : Rig){
        Log.i(TAG,"Opening Detail fragment")
        val detailFragment = DetailFragment(rig)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            //.hide(currentFragment)
            .replace(R.id.placeholder,detailFragment)
            .addToBackStack(null)
            .commit()
        currentFragment = detailFragment
    }

    fun openCapture(){
        if (ParseUser.getCurrentUser() == null) openLogin()
        else {
            actionBar.title = "Upload your Creation"
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                //.hide(currentFragment)
                .replace(R.id.placeholder,createFragment)
                .commit()
        }
        currentFragment = createFragment
    }

    fun openProfile(){
        if (ParseUser.getCurrentUser() == null) openLogin()
        else{
            val profileFragment = ProfileFragment(object : ProfileFragment.OnLogoutListener{
                override fun onLogout() {
                    ParseUser.logOutInBackground(){
                        deleteUserFile()
                        openLogin()
                    }
                }
            })
            actionBar.title = "${ParseUser.getCurrentUser().username}'s Profile"
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.placeholder,profileFragment)
                .commit()
            currentFragment = profileFragment
        }
    }

    fun openLogin(){
        actionBar.title = "Sign In"
        val loginFragment = LoginFragment(object : LoginFragment.OnLoginSuccessListener{
            override fun onLoginSuccess(username : String, password : String, remember : Boolean) {
                if (remember) saveCredentials(username, password)
                openProfile()
            }
        })
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(R.id.placeholder,loginFragment)
            .commit()
        currentFragment = loginFragment
    }

    // not exposed to user's other apps unless rooted in which case it is your problem
    private fun getUserFile(): File = File(filesDir,"user.dat")

    // no encryption because haha also what would stop someone from just reading the key?
    private fun saveCredentials(username:String, password:String){
        Log.i(TAG, "Cached credentials for $username")
        val file = File(filesDir,"user.dat")
        FileUtils.write(file,username+"\n", Charset.defaultCharset()) //overwrite
        FileUtils.write(file,password, Charset.defaultCharset(),true) //append
    }

    // returns null if no saved user
    private fun loadCredentials(): List<String>? {
        val file = getUserFile()
        try{
            val credentials = mutableListOf<String>()
            FileUtils.readLines(file, Charset.defaultCharset()).forEach { s : String ->
                credentials.add(s)
            }
            if (credentials.size == 2) {
                Log.i(TAG, "successfully loaded credentials from file")
                return credentials //success!
            }
            if (credentials.size == 0) {
                Log.i(TAG, "no credentials found, ready to create anew")
                return null
            }
            Log.e(TAG, "Credentials file corrupted! Deleting")
            file.delete()
            return null
        } catch(ioException: IOException){
            ParseUser.logOut() // get rid of the bad session - should not be preserved!
            ioException.printStackTrace() //uh oh - most likely the file does not exist right now
            return null
        }
    }
    // un-cache user credentials
    private fun deleteUserFile() = getUserFile().delete()

    companion object {
        const val TAG = "MainActivity"
        const val LOGIN_SUCCESS = "login success"
    }
}