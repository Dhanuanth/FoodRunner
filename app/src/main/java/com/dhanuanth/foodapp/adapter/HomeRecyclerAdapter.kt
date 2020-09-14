package com.dhanuanth.foodapp.adapter

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.activity.DetailsActivity
import com.dhanuanth.foodapp.activity.MainActivity
import com.dhanuanth.foodapp.database.RestDatabase
import com.dhanuanth.foodapp.database.RestEntities
import com.dhanuanth.foodapp.model.restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context,val restlist:ArrayList<restaurant>):RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){
    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val resname:TextView=view.findViewById(R.id.name)
        val resprice:TextView=view.findViewById(R.id.price)
        val resrating:TextView=view.findViewById(R.id.rating)
        val resimage:ImageView=view.findViewById(R.id.image)
        val resfav:ImageView=view.findViewById(R.id.fav)
        val llcontent:LinearLayout=view.findViewById(R.id.llcontent)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
    return restlist.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restentity=RestEntities(
            restlist[position].restid?.toInt(),
            restlist[position].restname,
            restlist[position].restprice,
            restlist[position].restrating,

            restlist[position].restimage

        )
        holder.resname.text=restlist[position].restname
        holder.resprice.text= " ${restlist[position].restprice}/person"
        holder.resrating.text=restlist[position].restrating
        Picasso.get().load(restlist[position].restimage).error(R.drawable.foodlogo).into(holder.resimage)
        if(!DBAsyncTask(context,restentity,mode = 1).execute().get())
        {
            Picasso.get().load(R.drawable.ic_fav_no_foreground.toString())
                .error(R.drawable.ic_fav_no_foreground).into(holder.resfav)
            holder.resfav.setTag("no")
        }
        else
        {
            Picasso.get().load(R.drawable.ic_fav_yes_foreground.toString())
                .error(R.drawable.ic_fav_yes_foreground).into(holder.resfav)
            holder.resfav.setTag("yes")


        }
        holder.resfav.setOnClickListener {

            if("no".equals(holder.resfav.getTag())) {
                Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show()
                Picasso.get().load(R.drawable.ic_fav_yes_foreground.toString())
                    .error(R.drawable.ic_fav_yes_foreground).into(holder.resfav)
                holder.resfav.setTag("yes")

                val insert=
                   DBAsyncTask(context, restentity, mode = 2).execute()
                val result=insert.get()

            }
            else {
                Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
                Picasso.get().load(R.drawable.ic_fav_no_foreground.toString())
                    .error(R.drawable.ic_fav_no_foreground).into(holder.resfav)
                holder.resfav.setTag("no")
                val delete=
                    DBAsyncTask(context, restentity, mode = 3).execute()
                val result=delete.get()

            }
            }
        holder.llcontent.setOnClickListener {

            val intent=Intent(context,DetailsActivity::class.java)
            intent.putExtra("text",holder.resname.text.toString())
            intent.putExtra("id", restlist[position].restid)
            context.startActivity(intent)



        }

        }
    class DBAsyncTask(val context:Context, val restentity: RestEntities, val mode:Int):
        AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(
            context,
            RestDatabase::class.java, "Restaurants"
        ).build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when (mode) {
                1 -> {
                    val rest: RestEntities? =
                        db.restDao().getRestbyId(restentity.rest_id.toString())
                    db.close()
                    return rest != null
                }
                2 -> {


                    db.restDao().insertRest(restentity)
                    db.close()

                    return true
                }
                3 -> {
                    db.restDao().deleteRest(restentity)
                    db.close()
                    return true
                }

            }
            return false

        }
    }


    }





