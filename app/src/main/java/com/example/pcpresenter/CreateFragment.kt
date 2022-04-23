package com.example.pcpresenter

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File

//TODO(move window when keyboard is up)

class CreateFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout fo this fragment
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        val imageView = view.findViewById<ImageView>(R.id.ivImage)
        imageView.setImageResource(R.drawable.ic_baseline_add_a_photo_24)
        imageView.findViewById<ImageView>(R.id.ivImage).setOnClickListener{
            onLaunchCamera()
        }

        view.findViewById<Button>(R.id.buttonSubmit).setOnClickListener{
            val name = view.findViewById<EditText>(R.id.etName).text.toString()
            val description = view.findViewById<EditText>(R.id.etDescription).text.toString()
            val cpu = view.findViewById<EditText>(R.id.etCpu).text.toString()
            val gpu = view.findViewById<EditText>(R.id.etGpu).text.toString()
            val mem = view.findViewById<EditText>(R.id.etMem).text.toString().toDoubleOrNull()
            //check inputs
            if (photoFile == null) {
                Toast.makeText(context, "Must include an image!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (name.isEmpty()){
                Toast.makeText(context, "Must include a name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (cpu.isEmpty()){
                Toast.makeText(context, "Must include your CPU!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (gpu.isEmpty()){
                Toast.makeText(context, "Must include your GPU!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mem == null){
                Toast.makeText(context, "Must include valid memory capacity!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            submitPost(name, description, photoFile!!, cpu, gpu, mem)
        }
        return view
    }

    fun submitPost(name : String, description :String, image : File, cpu : String, gpu : String, mem : Double){
        val newRig = Rig()
        newRig.setName(name)
        newRig.setDescription(description)
        newRig.setPhoto(ParseFile(image))
        newRig.setUploader(ParseUser.getCurrentUser())
        newRig.setCpu(cpu)
        newRig.setGpu(gpu)
        newRig.setMem(mem)

        newRig.saveInBackground(){ e ->
            if (e == null) {
                Log.i(MainActivity.TAG, "Successfully saved rig")
                Toast.makeText(context, "Rig submitted!", Toast.LENGTH_SHORT).show()
                requireView().findViewById<EditText>(R.id.tvName).text.clear()
                requireView().findViewById<EditText>(R.id.tvDescription).text.clear()
                requireView().findViewById<EditText>(R.id.tvCpu).text.clear()
                requireView().findViewById<EditText>(R.id.tvGpu).text.clear()
                requireView().findViewById<EditText>(R.id.tvMem).text.clear()
                requireView().findViewById<ImageView>(R.id.ivImage).setImageResource(R.drawable.ic_baseline_add_a_photo_24)
            }
            else Log.e(MainActivity.TAG, "Could not save post: ${e}")
        }
    }

    private val APP_TAG = "PC Presenter"
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    private val photoFileName = "photo.jpg"
    var photoFile: File? = null

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = requireView().findViewById(R.id.ivImage)
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Log.e(MainActivity.TAG, "No picture taken!")
            }
        }
    }
}