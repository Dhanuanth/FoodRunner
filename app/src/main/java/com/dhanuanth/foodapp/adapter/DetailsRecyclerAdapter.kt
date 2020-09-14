package com.dhanuanth.foodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.activity.CartActivity
import com.dhanuanth.foodapp.activity.DetailsActivity
import com.dhanuanth.foodapp.database.CartDatabase
import com.dhanuanth.foodapp.database.CartEntities

import com.dhanuanth.foodapp.model.Food
import com.dhanuanth.foodapp.model.restaurant

class DetailsRecyclerAdapter(val context: Context,val proceed:TextView,val rlcontent:RelativeLayout,var c:Int, val foodlist: ArrayList<Food>) :
    RecyclerView.Adapter<DetailsRecyclerAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val foodname: TextView = view.findViewById(R.id.foodname)
        val foodprice: TextView = view.findViewById(R.id.foodprice)
        val foodbutton: Button = view.findViewById(R.id.addbutton)
        val number: TextView = view.findViewById(R.id.number)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_details_single_row, parent, false)
        return DetailsViewHolder(view)
    }


    override fun getItemCount(): Int {
        return foodlist.size

    }

    @SuppressLint("ResourceAsColor", "ShowToast", "SetTextI18n")
    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {

        val cartentity = CartEntities(
            foodlist[position].foodid.toInt(),
            foodlist[position].foodname,
            foodlist[position].foodprice
        )
        holder.foodname.text = foodlist[position].foodname
        holder.foodprice.text = "Rs. ${foodlist[position].foodprice}"
        holder.number.text = (position + 1).toString()

        if (DBAsyncTask(context, cartentity, 1).execute().get()) {

            holder.foodbutton.text = "Remove"
            holder.foodbutton.background = ContextCompat.getDrawable(context, R.drawable.button_add_2)
        } else {
            holder.foodbutton.text = "Add"
            holder.foodbutton.background = ContextCompat.getDrawable(context, R.drawable.button_add)
        }

        holder.foodbutton.setOnClickListener {



            if (DBAsyncTask(context, cartentity, 1).execute().get()) {
                c-=foodlist[position].foodprice.toInt()

                holder.foodbutton.text = "Add"
                holder.foodbutton.background = ContextCompat.getDrawable(context, R.drawable.button_add)
                DBAsyncTask(context, cartentity, 3).execute().get()
            }
            else{
                c+=foodlist[position].foodprice.toInt()


                holder.foodbutton.text = "Remove"
                holder.foodbutton.background = ContextCompat.getDrawable(context, R.drawable.button_add_2)

                DBAsyncTask(context, cartentity, 2).execute().get()

            }
            if(DBAsyncTask(context,cartentity,4).execute().get()){
                proceed.text="( Rs. $c )"
                rlcontent.visibility=View.VISIBLE
            }
            else{
                rlcontent.visibility=View.GONE
            }

        }



    }

    class DBAsyncTask(val context: Context, val cartentity: CartEntities, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(
            context,
            CartDatabase::class.java, "Carts"
        ).build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when (mode) {
                1 -> {
                    val cart: CartEntities? =
                        db.cartDao().getCartbyId(cartentity.cart_id.toString())
                    db.close()
                    return cart != null
                }
                2 -> {


                    db.cartDao().insertCart(cartentity)
                    db.close()

                    return true
                }
                3 -> {
                    db.cartDao().deleteCart(cartentity)
                    db.close()
                    return true
                }
                4-> {
                    val cartno: List<CartEntities> = db.cartDao().getallCarts()
                    return cartno.isNotEmpty()
                }

            }
            return false

        }
    }
}