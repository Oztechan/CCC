/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.api

import android.content.Context
import com.github.mustafaozhan.data.R
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class ApiFactory(context: Context) {

    companion object {
        private const val TIME_OUT: Long = 3
        private const val MEDIA_TYPE = "application/json"
    }

    val endpoint = context.getString(R.string.backend_endpoint)

    val apiService: ApiService by lazy { initApiServices() }

    private fun initApiServices() = createRetrofit(getClient())
        .create(ApiService::class.java)

    private fun createRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(endpoint)
            .addConverterFactory(Json.asConverterFactory(MediaType.get(MEDIA_TYPE)))
            .client(httpClient)
            .build()
    }

    private fun getClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .build()
}
