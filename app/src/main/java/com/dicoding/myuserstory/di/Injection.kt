package com.dicoding.myuserstory.di

import android.content.Context
import com.dicoding.myuserstory.api.ApiConfig
import com.dicoding.myuserstory.data.StoryRepository
import com.dicoding.myuserstory.database.StoryDatabase

object Injection {
 fun provideRepository(context: Context) : StoryRepository{
     val database = StoryDatabase.getDatabase(context)
     val apiService = ApiConfig.getApiService(context)
     return StoryRepository(database, apiService)
 }
}