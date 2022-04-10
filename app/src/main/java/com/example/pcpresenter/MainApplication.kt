package com.example.pcpresenter

import android.app.Application
import android.content.Intent
import org.apache.commons.io.FileUtils

import android.util.Log
import com.parse.Parse
import com.parse.ParseAnonymousUtils
import com.parse.ParseObject
import com.parse.ParseUser
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Rig::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("QmHNmvEnsKqrW4HlsiyTiMznVbG4A90wsPFTXLyN")
                .clientKey("X12Bggb4KFhqU4oY7toivlqFxqS0VSeB9I7pIhGI")
                .server("https://parseapi.back4app.com")
                .build())
    }

    companion object {
        private const val TAG = "MainApplication"
    }

}