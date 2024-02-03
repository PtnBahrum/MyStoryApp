package com.dicoding.myuserstory.ui.main

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.myuserstory.data.StoryRepository
import com.dicoding.myuserstory.di.Injection
import com.dicoding.myuserstory.model.Story



class MainViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<Story>> = storyRepository.getStory().cachedIn(viewModelScope)
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}