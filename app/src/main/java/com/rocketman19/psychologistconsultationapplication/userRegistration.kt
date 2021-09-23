package com.rocketman19.psychologistconsultationapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class userRegistration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        //actionbar
        var actionbar = supportActionBar
        actionbar!!.title = "User Registration "
        //setting back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //initializing variables and finding 'elements' by id
        var email = findViewById<EditText>(R.id.inputEmailRegUser)
        var passsword = findViewById<EditText>(R.id.inputPasswordRegUser)
        var btnRegisterUser = findViewById<Button>(R.id.btnRegisterUser)

        //onClickListener for the register button in the form
        //register to firebase auth
        btnRegisterUser.setOnClickListener {
            when { //checking email and password are submitted empty
                TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(passsword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                } else -> { //if email and password are submitted, trim them and save in these variables
                    val userEmail:String = email.text.toString().trim { it <= ' ' }
                    val userPassword:String = passsword.text.toString().trim { it <= ' ' }

                    //creating an instance and registering the user with their email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
                        //checking if registration was successful
                        if (task.isSuccessful) {
                            //Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this,
                                "Registration successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            //directly signing in the user and sending them to the home page
                            val loginIntent = Intent(this, home::class.java)
                            //removing the layers of activities running in the background
                            loginIntent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            //loginIntent.putExtra("user_id", firebaseUser.uid) //add textView to home with id: user_id to see it if needed
                            loginIntent.putExtra("user_email_id", userEmail)
                            startActivity(loginIntent)
                            finish()
                        } else {
                            //if registration was unsuccessful, display error message
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

