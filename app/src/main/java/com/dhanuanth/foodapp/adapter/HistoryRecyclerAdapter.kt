package com.dhanuanth.foodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhanuanth.foodapp.R
import com.dhanuanth.foodapp.model.Food
import com.dhanuanth.foodapp.model.food_item
import com.dhanuanth.foodapp.model.orderhistory
import com.google.gson.Gson
import com.google.gson.JsonObject

class HistoryRecyclerAdapter(val context: Context, val foodlist: ArrayList<orderhistory>) :
    RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resname: TextView = view.findViewById(R.id.name)
        val date: TextView = view.findViewById(R.id.date)
val total:TextView=view.findViewById(R.id.total)
        val recyclerHome1:RecyclerView
                init {

                    recyclerHome1= view.findViewById(R.id.recyclerOrder) as RecyclerView

                }



    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_history_double_row, parent, false)

        return HistoryViewHolder(view)
    }


    override fun getItemCount(): Int {

        return foodlist.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        holder.resname.text = foodlist[position].restaurant_name
        holder.date.text = foodlist[position].date.substring(0,2)+"/"+foodlist[position].date.substring(3,5)+"/20"+foodlist[position].date.substring(6,8)

        holder.total.text= ": Rs." + foodlist[position].total_cost
        val listdata = arrayListOf<food_item>()
        for (i in 0 until foodlist[position].food_items.length()) {
            val obj = foodlist[position].food_items.getJSONObject(i)
            val food = food_item(
                obj.getString("food_item_id"),
                obj.getString("name"),
                obj.getString("cost")
            )

            listdata.add(food)
        }



        val recycleradapter:OrderRecyclerAdapter = OrderRecyclerAdapter(context, listdata)
        holder.recyclerHome1.layoutManager=LinearLayoutManager(context)
        holder.recyclerHome1.adapter=recycleradapter


    }


}




