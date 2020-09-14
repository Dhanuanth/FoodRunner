package com.dhanuanth.foodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.database.CartEntities
import com.dhanuanth.foodapp.model.Food
import kotlinx.android.synthetic.main.recycler_cart_single_row.view.*

class CartsRecyclerAdapter(val context: Context, val cartlist: List<CartEntities>) :
    RecyclerView.Adapter<CartsRecyclerAdapter.CartsViewHolder>() {


    class CartsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.cartname)
        val price: TextView = view.findViewById(R.id.cartprice)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_cart_single_row, parent, false)
        return CartsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartlist.size
    }

    override fun onBindViewHolder(holder: CartsViewHolder, position: Int) {

        holder.name.text = cartlist[position].food_name
        holder.price.text = "Rs. ${cartlist[position].food_price}"


    }

}