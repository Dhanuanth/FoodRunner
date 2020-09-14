package com.dhanuanth.foodapp.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.activity.LoginActivity
import com.dhanuanth.foodapp.activity.MainActivity


class ProfileFragment : Fragment() {
lateinit var logout:TextView
    lateinit var name:TextView
    lateinit var email:TextView
    lateinit var delivery:TextView
    lateinit var mobile:TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)
        name=view.findViewById(R.id.namereg)
        email=view.findViewById(R.id.emailaddress)
        delivery=view.findViewById(R.id.deliveryaddress)
        mobile=view.findViewById(R.id.mobilenumber)
        var sp: SharedPreferences? = activity?.getSharedPreferences(getString(R.string.shared_preferances_name),
            Context.MODE_PRIVATE)
        if(sp!=null) {
            name.text = sp.getString("Name", "none")
            email.text = sp.getString("Email", "none")
            delivery.text = sp.getString("Address", "none")
            mobile.text = "+91-"+sp.getString("Mobile", "none")
        }





        return view
    }


    }
