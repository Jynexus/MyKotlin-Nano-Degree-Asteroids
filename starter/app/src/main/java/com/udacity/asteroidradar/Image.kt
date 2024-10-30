package com.udacity.asteroidradar

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity (tableName = "Images")
data class Image(
    @PrimaryKey @ColumnInfo(name = "ID") val id : Int,
    @ColumnInfo (name="URL") val url: String,
    @ColumnInfo (name="Title") val title: String) : Parcelable
