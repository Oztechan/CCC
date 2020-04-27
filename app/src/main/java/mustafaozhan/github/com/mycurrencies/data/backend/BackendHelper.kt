// Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
package mustafaozhan.github.com.mycurrencies.data.backend

import android.content.Context
import com.github.mustafaozhan.basemob.api.BaseApiHelper
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.BuildConfig
import mustafaozhan.github.com.mycurrencies.R
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendHelper
@Inject constructor(
    private val context: Context
) : BaseApiHelper() {

    override val moshi: Moshi
        get() = Moshi.Builder().build()

    private val endPoint: String
        get() = context.getString(R.string.backend_endpoint)

    val backendService: BackendService by lazy { initBackendApiServices() }
    val backendServiceLongTimeOut: BackendService by lazy { initBackendApiServicesLongTimeOut() }

    private fun initBackendApiServices(): BackendService {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2L, TimeUnit.SECONDS)
            .writeTimeout(2L, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())

        val retrofit = initRxRetrofit(endPoint, clientBuilder.build())
        return retrofit.create(BackendService::class.java)
    }

    private fun initBackendApiServicesLongTimeOut(): BackendService {
        val clientBuilder = OkHttpClient.Builder().addInterceptor(getLoggingInterceptor())
        val retrofit = initRxRetrofit(endPoint, clientBuilder.build())
        return retrofit.create(BackendService::class.java)
    }

    private fun getLoggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.BASIC
        }
        return loggingInterceptor
    }
}
