package com.example.pcpresenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ProfileFragment(val onLogoutListener: OnLogoutListener) : Fragment() {

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

        return view
    }

}