package com.dicoding.myuserstory.api

import android.content.Context
import com.dicoding.myuserstory.utils.DataPref
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context): Interceptor {

    private val dataPref = DataPref(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        dataPref.getToken()?.let {
            request
                .addHeader("Authorization", "Bearer $it")
                .addHeader("Content-Type", "multiple/form-data")
                .build()
        }
        return chain.proceed(request.build())
    }
}