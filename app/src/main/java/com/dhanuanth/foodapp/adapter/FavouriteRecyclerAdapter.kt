package com.dhanuanth.foodapp.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.activity.DetailsActivity
import com.dhanuanth.foodapp.database.RestDatabase
import com.dhanuanth.foodapp.database.RestEntities
import com.dhanuanth.foodapp.model.restaurant
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context:Context,val restlist:List<RestEntities>):RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouritesViewHolder>(){
    class FavouritesViewHolder(view: View):RecyclerView.ViewHolder(view){
        val name: TextView =view.findViewById(R.id.name)
        val price: TextView =view.findViewById(R.id.price)
        val rating: TextView =view.findViewById(R.id.rating)
        val image: ImageView =view.findViewById(R.id.image)
        val resfav:ImageView=view.findViewById(R.id.fav)
        val llcontent:LinearLayout=view.findViewById(R.id.llcontent)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return FavouritesViewHolder(view)

    }

    override fun getItemCount(): Int {

        return restlist.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val restentity=RestEntities(
            restlist[position].rest_id?.toInt(),
            restlist[position].rest_name,
            restlist[position].rest_cost_for_one,
            restlist[position].rest_rating,

            restlist[position].rest_image

        )
        holder.name.text=restlist[position].rest_name
        holder.price.text=" ${restlist[position].rest_cost_for_one}/person"
        holder.rating.text=restlist[position].rest_rating

        //holder.image.setImageResource(itemList[position].bookimage)
        Picasso.get().load(restlist[position].rest_image).error(R.drawable.ic_home_foreground).into(holder.image)
        Picasso.get().load(R.drawable.ic_fav_yes_foreground.toString()).error(R.drawable.ic_fav_yes_foreground).into(holder.resfav)
        holder.resfav.tag = "yes"
        holder.resfav.setOnClickListener {

            if("no".equals(holder.resfav.getTag())) {

                Toast.makeText(context, "Added to favourites", Toast.LENGTH_LONG).show()
                Picasso.get().load(R.drawable.ic_fav_yes_foreground.toString())
                    .error(R.drawable.ic_fav_yes_foreground).into(holder.resfav)
                holder.resfav.tag = "yes"

                val insert=
                    HomeRecyclerAdapter.DBAsyncTask(context, restentity, mode = 2).execute()
                val result=insert.get()

            }
            else {

                Toast.makeText(context, "Removed from favourites", Toast.LENGTH_LONG).show()
                Picasso.get().load(R.drawable.ic_fav_no_foreground.toString())
                    .error(R.drawable.ic_fav_no_foreground).into(holder.resfav)
                holder.resfav.tag = "no"
                val delete=
                    HomeRecyclerAdapter.DBAsyncTask(context, restentity, mode = 3).execute()
                val result=delete.get()

            }
        }
        holder.llcontent.setOnClickListener {

            val intent=Intent(context, DetailsActivity::class.java)
            intent.putExtra("text",holder.name.text.toString())
            intent.putExtra("id", restlist[position].rest_id.toString())
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
