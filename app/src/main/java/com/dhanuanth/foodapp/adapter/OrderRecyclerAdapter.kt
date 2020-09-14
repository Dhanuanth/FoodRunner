package com.dhanuanth.foodapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.model.food_item
import com.dhanuanth.foodapp.model.orderhistory
import org.json.JSONArray
import org.json.JSONObject

class OrderRecyclerAdapter(val context: Context, val foodlist: ArrayList<food_item>) :
    RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder>() {
    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val food_name: TextView = view.findViewById(R.id.list)
        val price: TextView = view.findViewById(R.id.price)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_order_single_row, parent, false)
        return OrderViewHolder(view)

    }

    override fun getItemCount(): Int {
        return foodlist.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.food_name.text = foodlist[position].name
        holder.price.text = "Rs.${foodlist[position].cost}"
    }


}