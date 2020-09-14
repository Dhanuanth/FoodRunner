package com.dhanuanth.foodapp.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.adapter.FavouriteRecyclerAdapter
import com.dhanuanth.foodapp.adapter.HomeRecyclerAdapter
import com.dhanuanth.foodapp.database.RestDatabase
import com.dhanuanth.foodapp.database.RestEntities


class Favouritefragment : Fragment() {
lateinit var recyclerfavourites:RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    lateinit var restlist:List<RestEntities>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_favourite, container, false)
        recyclerfavourites=view.findViewById(R.id.recyclerFavourites)

        layoutManager= LinearLayoutManager(activity as Context)
        if(activity!=null){
            restlist=retrievefav(activity as Context).execute().get()
            if(restlist.size!=0) {
                recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, restlist)
                recyclerfavourites.adapter = recyclerAdapter
                recyclerfavourites.layoutManager = layoutManager
            }
            else
            {
                Toast.makeText(context,"Nothing to show",Toast.LENGTH_LONG).show()
            }



        }

        return view
    }
    class retrievefav(val context: Context): AsyncTask<Void, Void, List<RestEntities>>(){
        override fun doInBackground(vararg p0:Void?): List<RestEntities>{
            val db= Room.databaseBuilder(context,RestDatabase::class.java,"Restaurants").build()
            val restlist:List<RestEntities> = db.restDao().getallRest()
            return restlist
        }

    }



}