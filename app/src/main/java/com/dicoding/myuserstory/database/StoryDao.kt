package com.dicoding.myuserstory.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.myuserstory.model.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<Story>)

    @Query("SELECT * FROM stories")
    fun getAllStories(): PagingSource<Int, Story>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}