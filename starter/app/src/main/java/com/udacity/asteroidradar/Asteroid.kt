package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity (tableName = "Asteroids")
data class Asteroid(
    @ColumnInfo (name="ID") val id: Long,
    @PrimaryKey @ColumnInfo (name="Code_Name") val codename: String,
    @ColumnInfo (name="Close_App_Date") val closeApproachDate: String,
    @ColumnInfo (name="Abs_Magnitude") val absoluteMagnitude: Double,
    @ColumnInfo (name="Estimated_Dia")val estimatedDiameter: Double,
    @ColumnInfo (name="Rel_Velocity") val relativeVelocity: Double,
    @ColumnInfo (name="Distance_From_Earth") val distanceFromEarth: Double,
    @ColumnInfo (name="IsHazardous") val isPotentiallyHazardous: Boolean) : Parcelable