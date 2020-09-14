package com.dhanuanth.foodapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestDao {
    @Insert
    fun insertRest(restentities: RestEntities)
   @Delete
    fun deleteRest(restentities: RestEntities)
    @Query("SELECT * FROM Restaurants")
    fun getallRest():List<RestEntities>
    @Query("SELECT * FROM Restaurants WHERE rest_id=:restid")
    fun getRestbyId(restid:String):RestEntities

}