package com.rocketman19.psychologistconsultationapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.startActivityForResult
import java.io.IOException

class therapistRegistration : AppCompatActivity() {

    private var mySelectedImageFileUri: Uri? = null

    private lateinit var btnSelectImage:Button
    private lateinit var imageView:ImageView

    /*companion object {
        val IMAGE_REQUEST_CODE = 100
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_registration)

        //actionbar
        var actionbar = supportActionBar
        actionbar!!.title = "Therapist Registration"
        //setting back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //initializing variables and finding 'elements' by id
        // registration form textInputEditTexts
        var name = findViewById<EditText>(R.id.inputNameRegTherapist)
        var phone = findViewById<EditText>(R.id.inputPhoneRegTherapist)
        var email = findViewById<EditText>(R.id.inputEmailRegTherapist)
        var location = findViewById<EditText>(R.id.inputLocationRegTherapist)
        var qualification = findViewById<EditText>(R.id.inputQualificationRegTherapist)
        var passsword = findViewById<EditText>(R.id.inputPasswordRegTherapist)
        var btnRegisterTherapist = findViewById<Button>(R.id.btnRegisterTherapist)
        btnSelectImage = findViewById(R.id.btnInsertPic)
        imageView = findViewById(R.id.inputTherapistPic)

        //inserting img on button click
        /*btnSelectImage.setOnClickListener {
            pickImageGallery()
        }*/

        //SELECTING IMAGE
        btnSelectImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //select image
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 222)
            } else {
                //request permission from user
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 121)
            }
        }

        //UPLOADING IMAGE to firebase storage and SAVING DATA to real time database
        btnRegisterTherapist.setOnClickListener {
            if (mySelectedImageFileUri != null) {
                val imageExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(mySelectedImageFileUri!!))
                val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("therapist`" + System.currentTimeMillis() + "." + imageExtension)
                storageRef.putFile(mySelectedImageFileUri!!).addOnSuccessListener {uploadTask->
                    uploadTask.metadata!!.reference!!.downloadUrl.addOnSuccessListener {url->
                        var jina = name.text.toString().trim()
                        var simu = phone.text.toString().trim()
                        var arafa = email.text.toString().trim()
                        var cheo = qualification.text.toString().trim()
                        var pahali = location.text.toString().trim()
                        var siri = passsword.text.toString().trim()
                        //checking if all input fields have been filled
                        if (cheo.isEmpty() || pahali.isEmpty() || simu.isEmpty() || jina.isEmpty() || arafa.isEmpty() || siri.isEmpty()) {
                            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
                        } else {
                            //continue to save therapist's data
                            //time the therapist registered in milliseconds
                            var time = System.currentTimeMillis().toString()
                            var imageUrl =url
                            //data to be saved
                            var customTherapist = appTherapist(jina,arafa,simu,pahali,cheo,siri,time,imageUrl.toString())
                            //creating a table in the firebase database
                            var ref: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("appTherapists/$time")
                            //storing data in the reference and checking to see if the data was saved
                            ref.setValue(customTherapist).addOnCompleteListener { task->
                                if (task.isSuccessful){
                                    name.setText("")
                                    phone.setText("")
                                    email.setText("")
                                    qualification.setText("")
                                    location.setText("")
                                    passsword.setText("")
                                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_LONG).show()
                                }else {
                                    Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                }
            }else{
                Toast.makeText(this, "Please select an image to upload", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 121) {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 222)
        } else {
            Toast.makeText(this,
                "Oops, you just denied the permission for storage. You can allow it from the settings"
                ,Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data !== null) {
                try {
                    mySelectedImageFileUri = data.data!!
                    imageView.setImageURI(mySelectedImageFileUri)
                }catch (e:IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Image selection failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //private fun pickImageGallery() {
      //  val intent = Intent(Intent.ACTION_PICK)
       // intent.type = "image/*"
       // startActivityForResult(intent, IMAGE_REQUEST_CODE)
    //}

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            imageView.setImageURI(data?.data)
        }
    }*/

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}