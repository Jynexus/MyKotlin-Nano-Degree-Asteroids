package com.udacity.asteroidradar

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET


val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL).build()

val retrofitImage = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL_APOD).build()

interface AstroApiService {
    @GET("feed")
    fun getProperties(@retrofit2.http.Query("start_date") startDate : String , @retrofit2.http.Query("end_date") endDate : String, @retrofit2.http.Query("api_key") apiKey : String) : Deferred<String>
}

interface AstroImageService {
    @GET("apod")
    fun getImage(@retrofit2.http.Query("api_key") apiKey : String): Deferred<String>
}

object AstroApi {
    val retrofitService: AstroApiService by lazy {
        retrofit.create(AstroApiService::class.java)
    }
}
object AstroImage{
        val retrofitService :AstroImageService by lazy {
            retrofitImage.create(AstroImageService :: class.java)
        }
}