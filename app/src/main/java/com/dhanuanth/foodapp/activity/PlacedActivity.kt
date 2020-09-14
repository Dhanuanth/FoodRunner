package com.dhanuanth.foodapp.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.database.CartDatabase
import java.util.Calendar.getInstance

class PlacedActivity:AppCompatActivity() {
    lateinit var button:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_placed)
        button=findViewById(R.id.ok)
        button.setOnClickListener {

            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }


    override fun onBackPressed() {
        Toast.makeText(this,"click OK to continue",Toast.LENGTH_LONG).show()
    }

    override fun finish() {


        super.finish()
    }



}