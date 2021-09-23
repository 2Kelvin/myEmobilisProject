package com.rocketman19.psychologistconsultationapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide


class CustomAdapter(var context: Context, var data: ArrayList<appTherapist>):BaseAdapter() {
    private class ViewHolder(row:View?){
        var tName:TextView
        var tQualification:TextView
        var tLocation:TextView
        var tProfilePic:ImageView
        var tCall:ImageView
        var tText:ImageView
        init {
            this.tName = row?.findViewById(R.id.therapistName) as TextView
            this.tQualification = row?.findViewById(R.id.therapistQualification) as TextView
            this.tLocation = row?.findViewById(R.id.therapistLocation) as TextView
            this.tProfilePic = row?.findViewById(R.id.profilePic) as ImageView
            this.tCall = row?.findViewById(R.id.callTherapist) as ImageView
            this.tText = row?.findViewById(R.id.textTherapist) as ImageView
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewHolder:ViewHolder
        if (convertView == null){
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.therapist_card_layout,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var item:appTherapist = getItem(position) as appTherapist
        viewHolder.tName.text = item.inputName
        viewHolder.tQualification.text = item.inputQualification
        viewHolder.tLocation.text = item.inputLocation
        Glide.with(context)
            .load(item.inputPhoto)
            .into(viewHolder.tProfilePic)
        viewHolder.tCall.setImageResource(R.drawable.dial)
        viewHolder.tText.setImageResource(R.drawable.text)
        viewHolder.tCall.setOnClickListener {
            makeCall(item.inputPhone,context)
        }
        viewHolder.tText.setOnClickListener {
            sendSms(item.inputPhone,context)
        }
        return view as View
    }

    override fun getItem(position: Int): Any {
        return  data.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.count()
    }
    fun makeCall(phoneNumber:String, context: Context){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "$phoneNumber"))
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        } else {
            context.startActivity(intent)
        }
    }
    fun sendSms(phoneNumber:String, context: Context){
        val uri = Uri.parse("smsto:$phoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", "")
        context.startActivity(intent)
    }
}