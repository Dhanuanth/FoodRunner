package com.dhanuanth.foodapp.model

import org.json.JSONArray

data class orderhistory(

    val order_id:String,
    val restaurant_name:String,
val total_cost:String,
    val date:String,
    val food_items: JSONArray
)

data class food_item (
    val food_item_id:String,
    val name:String,
    val cost:String

)
