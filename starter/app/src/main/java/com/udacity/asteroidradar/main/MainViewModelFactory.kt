package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.AsteroidDAO
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private  val dataSource : AsteroidDAO,
    private  val application: Application,
    private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel:: class.java)){
            return MainViewModel(dataSource,application,context) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel Class")
    }
}