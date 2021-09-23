package com.rocketman19.psychologistconsultationapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class userLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        //initializing variables
        var actionbar = supportActionBar
        var btnRegTherapist = findViewById<Button>(R.id.therRegWithUs)
        val btnLoginUser = findViewById<Button>(R.id.btnLoginUser)
        var email = findViewById<EditText>(R.id.inputEmailLoginUser)
        var password = findViewById<EditText>(R.id.inputPassLoginUser)

        //actionbar
        actionbar!!.title = "User Login"
        //setting back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //user registration form link
        var userRegFormLink = findViewById<TextView>(R.id.userRegFormLink)
        userRegFormLink.setOnClickListener {
            var userRegIntent = Intent(this, userRegistration::class.java)
            startActivity(userRegIntent)
        }

        //to therapist registration form
        btnRegTherapist.setOnClickListener {
            var regFormT = Intent(this, therapistRegistration::class.java)
            startActivity(regFormT)
        }

        //login user into app
        btnLoginUser.setOnClickListener {
            when { //checking email and password are submitted empty
                TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                } else -> { //if email and password are submitted, trim them and save in these variables
                val userEmail:String = email.text.toString().trim { it <= ' ' }
                val userPassword:String = password.text.toString().trim { it <= ' ' }

                //logging in user from firebase
                FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
                    //checking if registration was successful
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        Toast.makeText(
                            this,
                            "Welcome",
                            Toast.LENGTH_SHORT
                        ).show()
                        //directly signing in the user and sending them to the home page
                        val loginIntent = Intent(this, home::class.java)
                        //removing the layers of activities running in the background
                        loginIntent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        //loginIntent.putExtra("user_id", FirebaseAuth.getInstance().currentUser.uid) //add user's firebase ID to textView in homeActivity with an id of:user_id
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