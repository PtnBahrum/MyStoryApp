package com.dicoding.myuserstory.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.myuserstory.api.ApiService
import com.dicoding.myuserstory.database.StoryDatabase
import com.dicoding.myuserstory.model.Story

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory() : LiveData<PagingData<Story>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }
}