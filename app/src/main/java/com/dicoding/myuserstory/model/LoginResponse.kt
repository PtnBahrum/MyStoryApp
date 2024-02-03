package com.dicoding.myuserstory.model

data class LoginResponse(
    val error : Boolean,
    val message : String,
    val loginResult : UserResponse
)
data class UserResponse(
    val userId : String,
    val name : String,
    val token : String,
)
