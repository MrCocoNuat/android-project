package com.example.pcpresenter

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Rig")
class Rig : ParseObject() {
    fun getUploader() : ParseUser = getParseUser(KEY_UPLOADER)!!
    fun getPhoto() : ParseFile = getParseFile(KEY_PHOTO)!!
    fun getName() : String = getString(KEY_NAME)!!

    fun getDescription() : String? = getString(KEY_DESCRIPTION)

    fun setUploader(user : ParseUser) = put(KEY_UPLOADER, user)
    fun setPhoto(photo: ParseFile) = put(KEY_PHOTO, photo)
    fun setName(name : String) = put(KEY_NAME, name)

    fun setDescription(description : String) = put(KEY_DESCRIPTION, description)

    companion object{
        const val KEY_UPLOADER = "uploader"
        const val KEY_PHOTO = "photo"
        const val KEY_NAME = "name"
        const val KEY_DESCRIPTION = "description"
    }
}
