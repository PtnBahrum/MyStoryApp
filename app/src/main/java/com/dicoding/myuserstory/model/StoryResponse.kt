package com.dicoding.myuserstory.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    val error : Boolean?,
    val message : String,
    val listStory : ArrayList<Story>
)
@Parcelize
@Entity(tableName = "stories")
data class Story(
    @PrimaryKey
    val id : String,
    val name : String,
    val description : String,
    val photoUrl : String,
    val createdAt : String,
    val lat : Float,
    val lon : Float,
):Parcelable

data class UploadResponse(
    val error  :Boolean,
    val message : String
)