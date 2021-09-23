package com.rocketman19.psychologistconsultationapplication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class home : AppCompatActivity() {

    //initializing variables
    var therapistListView: ListView? = null
    var allTherapists:ArrayList<appTherapist>? = null
    var adapter:CustomAdapter? = null
    //var progress:ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //actionbar
        var actionbar = supportActionBar
        actionbar!!.title = "Home"
        //setting back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //finding variables by id
        therapistListView = findViewById(R.id.therapistListView)
        allTherapists = ArrayList()
        adapter = CustomAdapter(this, allTherapists!!)
        //progress = ProgressDialog(this)
        //progress!!.setTitle("Loading")
        //progress!!.setMessage("Please wait...")


        var findTherapist = findViewById<Button>(R.id.buttonFindTherapist)
        val logout = findViewById<TextView>(R.id.tvLogout)
        val user_email_id = findViewById<TextView>(R.id.user_email_id)

        val fbEmailId = intent.getStringExtra("user_email_id")
        //displaying user_email in our textView
        user_email_id.text = "Email:  $fbEmailId"

        //logging out the user from the app
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, userLogin::class.java))
            finish()
        }

        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("appTherapists")
        //progress!!.show()
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allTherapists!!.clear()

                //looping through fetched data and displaying it
                for (snap in snapshot.children) {
                    var appTherapist = snap.getValue(appTherapist::class.java)
                    allTherapists!!.add(appTherapist!!)
                }
                adapter!!.notifyDataSetChanged()
                //progress!!.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@home, "Please contact ADMIN for assistance", Toast.LENGTH_LONG)
            }
        })
        therapistListView!!.adapter = adapter

        //setting onClickListeners
       findTherapist.setOnClickListener {
           //opens viewAllTherapists activity/page on 'view all' click
           var viewAllIntent = Intent(this, viewAllTherapists::class.java)
           startActivity(viewAllIntent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}