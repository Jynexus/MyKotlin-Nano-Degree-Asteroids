package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.view.MenuItem
import androidx.lifecycle.*
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class MainViewModel(
    val database : AsteroidDAO, application: Application, val context: Context) : AndroidViewModel(application) {

    lateinit var asteriods : LiveData<MutableList<Asteroid>>

    val dateList :ArrayList<String> = getNextSevenDaysFormattedDates()

    lateinit var image : LiveData <Image>

    init {
            update()
            updateImage()
    }

         fun updateImage(){
            val ConnectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = ConnectionManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                viewModelScope.launch {
                    var getImageDeferred = AstroImage.retrofitService.getImage(Constants.CUST_KEY)
                    try {
                        var jsonObjString = getImageDeferred.await()
                        var jsonObj = JSONObject(jsonObjString)
                        var mediaType = jsonObj.getString("media_type")
                        if(mediaType == "image"){
                            var url = jsonObj.getString("url")
                            var title = jsonObj.getString("title")
                            viewModelScope.launch {insertImage(Image(1,url,title))}
                        }
                    }
                    catch (t: Throwable) { }
                }
            }

             viewModelScope.launch { getImage() }
         }

        fun update(){
            val ConnectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = ConnectionManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                viewModelScope.launch {
                    var getPropsDeferred  = AstroApi.retrofitService.getProperties(dateList.first(),dateList.last(),Constants.CUST_KEY)
                    try {
                        var jsonObjString = getPropsDeferred.await()
                        var arrayListAstro = parseAsteroidsJsonResult(JSONObject(jsonObjString))
                        arrayListAstro.forEach{ viewModelScope.launch { insert(it) }}
                    }
                    catch (t : Throwable){ }
                }
            }

            viewModelScope.launch { getAllAsteroids() }
        }

        suspend fun insertImage(image: Image){
        withContext(Dispatchers.IO)  {
            dbInsertImage(image)
        }
    }

        suspend fun insert(asteroid : Asteroid){
            withContext(Dispatchers.IO)  {
                dbInsert(asteroid)
            }
        }

        suspend fun getTodayAstro(){
                dbGetToday()
        }

        suspend fun getPeriodAstro( ){
            dbGetPeriod()
        }

        suspend fun  getAllAsteroids(){
                dbGetAll()
        }

        suspend fun  getImage(){
        dbGetImage()
        }






    // repo
    private suspend fun dbInsertImage (image: Image){
        withContext(Dispatchers.IO) { database.insert(image) }
    }
    private suspend fun dbGetImage() {
        image = database.getImage()
    }

    private suspend fun dbInsert (asteroid: Asteroid){
        withContext(Dispatchers.IO) { database.insert(asteroid) }
    }
    private suspend fun  dbGetToday() {
         asteriods = database.getTodayAstro("'"+ dateList.first()+"'")
    }
    private suspend fun dbGetPeriod (){


        var firstString = dateList[0].toString()

        var secondString = dateList[1].toString()

        var thirdString = dateList[2].toString()

        var fourthString = dateList[3].toString()

        var fifthString = dateList[4].toString()

        var sixthString = dateList[5].toString()

        var seventhString = dateList[6].toString()


        asteriods = database.getPeriodAstro(firstString,secondString,thirdString,fourthString,fifthString,sixthString,seventhString)
    }
    private suspend fun dbGetAll() {
        asteriods = database.getAllAsteroids()
    }

    fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
        val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

        val asteroidList = ArrayList<Asteroid>()

        val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
        for (formattedDate in nextSevenDaysFormattedDates) {
            if (nearEarthObjectsJson.has(formattedDate)) {
                val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

                for (i in 0 until dateAsteroidJsonArray.length()) {
                    val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                    val id = asteroidJson.getLong("id")
                    val codename = asteroidJson.getString("name")
                    val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                    val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                        .getJSONObject("kilometers").getDouble("estimated_diameter_max")

                    val closeApproachData = asteroidJson
                        .getJSONArray("close_approach_data").getJSONObject(0)
                    val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                        .getDouble("kilometers_per_second")
                    val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                        .getDouble("astronomical")
                    val isPotentiallyHazardous = asteroidJson
                        .getBoolean("is_potentially_hazardous_asteroid")

                    val asteroid = Asteroid(id, codename, formattedDate, absoluteMagnitude,
                        estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
                    asteroidList.add(asteroid)
                }
            }
        }

        return asteroidList
    }

    fun getNextSevenDaysFormattedDates(): ArrayList<String> {
        val formattedDateList = ArrayList<String>()

        val calendar = Calendar.getInstance()
        for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
            val currentTime = calendar.time
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            formattedDateList.add(dateFormat.format(currentTime))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return formattedDateList
    }


}