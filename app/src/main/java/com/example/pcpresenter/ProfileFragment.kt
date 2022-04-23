package com.example.pcpresenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.parse.ParseUser

class ProfileFragment(
    val onLogoutListener: OnLogoutListener,
    //val fragmentManager: FragmentManager
    ) : Fragment() {

    interface OnLogoutListener{
        fun onLogout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        view.findViewById<Button>(R.id.buttonLogout).setOnClickListener{ onLogoutListener.onLogout() }

        val fragmentToShow = RigFeedFragment(
            ParseUser.getCurrentUser(),
            MainActivity.openDetailFragmentNotifier,
            MainActivity.openFeedFragmentNotifier
        )

        requireFragmentManager().beginTransaction()
            .replace(R.id.placeholder2, fragmentToShow)
            .commit()


        return view
    }

}