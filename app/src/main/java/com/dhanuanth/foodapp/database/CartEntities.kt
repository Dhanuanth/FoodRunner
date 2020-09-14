package com.dhanuanth.foodapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cart")
data class CartEntities (
    @PrimaryKey val cart_id:Int,

    @ColumnInfo(name="name") val food_name:String,
    @ColumnInfo(name = "cost")val food_price:String


)