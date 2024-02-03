package com.dicoding.myuserstory.ui.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myuserstory.api.ApiConfig
import com.dicoding.myuserstory.model.LoginResponse
import com.dicoding.myuserstory.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private var _user = MutableLiveData<LoginResponse>()
    val user : LiveData<LoginResponse> = _user

    private var _state = MutableLiveData<Boolean>()
    val state : LiveData<Boolean> = _state

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun login(context: Context,email :String, password : String){
        _isLoading.value = true
        ApiConfig.getApiService(context)
            .login(email,password)
            .enqueue(object: Callback<LoginResponse> {
                override fun onResponse(call : Call<LoginResponse>, response: Response<LoginResponse>){
                    _isLoading.value = false
                    if(response.isSuccessful){
                        _user.postValue(response.body())
                        _state.postValue(true)
                    }else{
                        _state.postValue(false)
                    }
                }
                override fun onFailure(call : Call<LoginResponse>, t : Throwable){
                    _isLoading.value = false
                    t.message?.let{ Log.d("Failure",it)}
                }
                
            })
    }
    fun register(context: Context,name :String, email: String, password: String){
        _isLoading.value = true
        ApiConfig.getApiService(context)
            .register(name, email, password)
            .enqueue(object :Callback<RegisterResponse>{
                override fun onResponse( call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    _isLoading.value = false
                    if(response.isSuccessful){
                        _state.postValue(true)
                    }else{
                        _state.postValue(true)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    t.message?.let{ Log.d("Failure",it)}
                }
            })
    }
}