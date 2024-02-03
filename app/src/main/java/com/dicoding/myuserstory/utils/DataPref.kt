package com.dicoding.myuserstory.utils

import android.content.Context
import android.content.SharedPreferences

class DataPref(context: Context) {

    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = sharedPreferences.edit()

    fun setUser(id : String, name : String, token : String){
        editor.putBoolean("LOGIN",true)
        editor.putString("ID", id)
        editor.putString("NAME", name)
        editor.putString("TOKEN", token)
        editor.apply()
    }
    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN",null)
    }

    fun getName() : String? {
        return sharedPreferences.getString("NAME",null)
    }

    fun isLogin() : Boolean{
        return sharedPreferences.getBoolean("LOGIN",false)
    }

    fun removeUser(){
        editor.clear().apply()
    }
}