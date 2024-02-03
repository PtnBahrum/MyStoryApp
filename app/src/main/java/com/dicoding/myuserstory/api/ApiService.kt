package com.dicoding.myuserstory.api

import com.dicoding.myuserstory.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name : String,
        @Field("email") email: String,
        @Field("password") password : String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Part file : MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<UploadResponse>

    @Multipart
    @POST("stories")
    fun postStoryWithLocation(
        @Part file : MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: Double,
        @Part("lon") longitude:Double,
    ): Call<UploadResponse>

    @Multipart
    @POST("stories/guest")
    fun postStoryGuest(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ):Call<UploadResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("stories")
    fun getAllStoriesWithLocation(
        @Query("location") page : Int = 1
    ): Call<StoryResponse>

}