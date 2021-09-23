package com.rocketman19.psychologistconsultationapplication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class viewAllTherapists : AppCompatActivity() {

    //initializing variables
    var therapistListView: ListView? = null
    var allTherapists:ArrayList<appTherapist>? = null
    var adapter:CustomAdapter? = null
    var progress:ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_therapists)

        //finding Views byId
        therapistListView = findViewById(R.id.therapistListView)
        allTherapists = ArrayList()
        adapter = CustomAdapter(this, allTherapists!!)
       // progress = ProgressDialog(this)
        //progress!!.setTitle("Loading")
        //progress!!.setMessage("Please wait...")

        var actionbar = supportActionBar

        //actionbar
        actionbar!!.title = "All Therapists"
        //setting back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("appTherapists")
       // progress!!.show()
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
                Toast.makeText(this@viewAllTherapists, "Please contact ADMIN for assistance", Toast.LENGTH_LONG)
            }
        })
        therapistListView!!.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}