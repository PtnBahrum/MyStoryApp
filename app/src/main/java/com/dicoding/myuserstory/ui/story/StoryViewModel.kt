package com.dicoding.myuserstory.ui.story

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myuserstory.api.ApiConfig
import com.dicoding.myuserstory.model.UploadResponse
import retrofit2.Callback
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class StoryViewModel : ViewModel() {

    private var _state  = MutableLiveData<Boolean>()
    val state : LiveData<Boolean> = _state

    fun postStory(imgMultipart : MultipartBody.Part, description: RequestBody, context: Context){
        ApiConfig.getApiService(context)
            .postStory(imgMultipart, description)
            .enqueue(object : Callback<UploadResponse>{
                override fun onResponse( call: Call<UploadResponse>, response: Response<UploadResponse>){
                    if(response.isSuccessful){
                        if(response.body() != null && !response.body()!!.error){
                            _state.postValue(true)
                        }
                    }else{
                        _state.postValue(false)
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    t.message?.let{ Log.d("Failure Post",it)}
                }
            })
    }

    fun postStoryWithLocation(imgMultipart: MultipartBody.Part, description: RequestBody, lat: Double, lon: Double, context: Context){
        ApiConfig.getApiService(context)
            .postStoryWithLocation(imgMultipart,description,lat,lon)
            .enqueue(object : Callback<UploadResponse>{
                override fun onResponse( call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if (response.isSuccessful){
                        if (response.body() != null && !response.body()!!.error) {
                            _state.postValue(true)
                        }
                    } else {
                        _state.postValue(false)
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure Post With Story",it) }
                }
            })
    }

    fun postStoryForGuest(context: Context,imageMultipart: MultipartBody.Part, description: RequestBody){
        ApiConfig.getApiService(context)
            .postStoryGuest(imageMultipart, description)
            .enqueue(object : Callback<UploadResponse>{
                override fun onResponse( call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if(response.isSuccessful){
                        if(response.body() != null && !response.body()!!.error){
                            _state.postValue(true)
                        }
                    }else{
                        _state.postValue(false)
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    t.message?.let{ Log.d("Failure",it)}
                }
            })
    }
}