package com.dhanuanth.foodapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurants")
data class RestEntities (
    @PrimaryKey val rest_id:Int,
    @ColumnInfo(name="name") val rest_name:String,
    @ColumnInfo(name = "cost")val rest_cost_for_one:String,
    @ColumnInfo(name = "rating")val rest_rating:String,

    @ColumnInfo(name="image")val rest_image:String
)