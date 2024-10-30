package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.sql.Date


@Dao
interface AsteroidDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (asteroid: Asteroid)

    @Query("SELECT * from Asteroids ORDER BY ID DESC")
    fun getAllAsteroids () : LiveData <MutableList<Asteroid>>

    @Query("SELECT * from Asteroids Where ID = :key")
    fun getAsteroidById (key : Long) : Asteroid

    @Query("SELECT * from Asteroids Where  Close_App_Date = :date ")
    fun getTodayAstro (date:String) : LiveData <MutableList<Asteroid>>

    @Query("SELECT * from Asteroids Where  Close_App_Date Like :firstDate or Close_App_Date Like :secondDate or Close_App_Date = :thirdDate or Close_App_Date = :fourthDate or Close_App_Date = :fifthDate or Close_App_Date = :sixthDate or Close_App_Date = :seventhDate ")
    fun getPeriodAstro (firstDate :String , secondDate :String, thirdDate :String, fourthDate : String, fifthDate :String, sixthDate : String, seventhDate :String) : LiveData <MutableList<Asteroid>>

    @Query("DELETE from Asteroids")
    fun clear ()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (image: Image)

    @Query ("SELECT * from Images LIMIT 1")
    fun getImage() : LiveData <Image>

}