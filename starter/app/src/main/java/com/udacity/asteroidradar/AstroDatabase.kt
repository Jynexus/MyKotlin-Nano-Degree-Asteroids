package com.udacity.asteroidradar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Asteroid::class , Image::class], version = 3, exportSchema = false)
abstract class  AstroDatabase : RoomDatabase(){


    abstract  val AstroDatabaseDAO :AsteroidDAO
    companion object  {
        @Volatile
        private var INSTANCE : AstroDatabase? = null
        fun getInstance(context: Context): AstroDatabase {
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance =  Room.databaseBuilder(context.applicationContext,
                    AstroDatabase :: class.java,
                    "AsteroidsDB")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}