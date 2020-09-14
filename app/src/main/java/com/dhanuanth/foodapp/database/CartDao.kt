package com.dhanuanth.foodapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CartDao {
    @Insert
    fun insertCart(cartentities: CartEntities)
    @Delete
    fun deleteCart(cartentities: CartEntities)
    @Query("SELECT * FROM Cart")
    fun getallCarts():List<CartEntities>
    @Query("SELECT * FROM Cart WHERE cart_id=:cartid")
    fun getCartbyId(cartid:String):CartEntities
    @Query("DELETE FROM Cart ")
    fun deleteallCarts():Int
    @Query("SELECT cost FROM Cart")
    fun getTotalPrice():Array<String>
    @Query("SELECT cart_id FROM Cart")
    fun getTotalFood():Array<Int>


}