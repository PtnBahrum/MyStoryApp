package com.dicoding.myuserstory.ui.maps

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myuserstory.api.ApiConfig
import com.dicoding.myuserstory.model.Story
import com.dicoding.myuserstory.model.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel() {

    private val _stories = MutableLiveData<ArrayList<Story>>()
    val stories : LiveData<ArrayList<Story>> = _stories

    fun getStories(context: Context) {
        ApiConfig.getApiService(context)
            .getAllStoriesWithLocation()
            .enqueue(object: Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    if(response.isSuccessful) {
                        _stories.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }
            })
    }
}