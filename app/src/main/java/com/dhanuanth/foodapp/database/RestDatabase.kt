package com.dhanuanth.foodapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [RestEntities::class],version = 1)
abstract class RestDatabase :RoomDatabase(){
    abstract fun restDao(): RestDao

}
