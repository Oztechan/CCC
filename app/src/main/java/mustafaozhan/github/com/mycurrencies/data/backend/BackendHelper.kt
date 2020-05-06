/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.backend

import android.content.Context
import com.github.mustafaozhan.basemob.api.BaseApiHelper
import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.R
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

    private val interceptor: HttpLoggingInterceptor
        get() = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    val backendService: BackendService by lazy { initBackendApiServices() }
    val backendServiceLongTimeOut: BackendService by lazy { initBackendApiServicesLongTimeOut() }

    private fun initBackendApiServices(): BackendService {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2L, TimeUnit.SECONDS)
            .writeTimeout(2L, TimeUnit.SECONDS)
            .addInterceptor(interceptor)

        val retrofit = initRxRetrofit(endPoint, clientBuilder.build())
        return retrofit.create(BackendService::class.java)
    }

    private fun initBackendApiServicesLongTimeOut(): BackendService {
        val clientBuilder = OkHttpClient.Builder().addInterceptor(interceptor)
        val retrofit = initRxRetrofit(endPoint, clientBuilder.build())
        return retrofit.create(BackendService::class.java)
    }
}
