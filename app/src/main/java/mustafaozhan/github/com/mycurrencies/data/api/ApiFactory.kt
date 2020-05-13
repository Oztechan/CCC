/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.api

import android.content.Context
import com.github.mustafaozhan.basemob.api.BaseApiFactory
import mustafaozhan.github.com.mycurrencies.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiFactory
@Inject constructor(
    private val context: Context
) : BaseApiFactory() {

    val apiService: ApiService by lazy { initApiServices() }
    val apiServiceLongTimeOut: ApiService by lazy { initApiServicesLongTimeOut() }

    override val endpoint: String
        get() = context.getString(R.string.backend_endpoint)

    private val interceptor: HttpLoggingInterceptor
        get() = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    private fun initApiServices(): ApiService {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2L, TimeUnit.SECONDS)
            .writeTimeout(2L, TimeUnit.SECONDS)
            .addInterceptor(interceptor)

        return createRetrofit(clientBuilder.build())
            .create(ApiService::class.java)
    }

    private fun initApiServicesLongTimeOut(): ApiService {
        val clientBuilder = OkHttpClient.Builder().addInterceptor(interceptor)
        return createRetrofit(clientBuilder.build())
            .create(ApiService::class.java)
    }
}
