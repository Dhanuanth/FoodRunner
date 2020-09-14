package com.dhanuanth.foodapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartEntities::class],version = 1)
abstract class CartDatabase : RoomDatabase(){
    abstract fun cartDao(): CartDao

}