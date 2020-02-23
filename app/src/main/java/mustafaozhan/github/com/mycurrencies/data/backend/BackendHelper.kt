package mustafaozhan.github.com.mycurrencies.data.backend

import com.squareup.moshi.Moshi
import mustafaozhan.github.com.mycurrencies.BuildConfig
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.api.BaseApiHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Singleton
class BackendHelper @Inject
constructor() : BaseApiHelper() {

    override val moshi: Moshi
        get() = Moshi.Builder().build()

    val backendService: BackendService by lazy { initBackendApiServices() }

    private fun initBackendApiServices(): BackendService {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1L, TimeUnit.SECONDS)
            .writeTimeout(1L, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())

        val endpoint = getString(R.string.backend_endpoint)
        val retrofit = initRxRetrofit(endpoint, clientBuilder.build())
        return retrofit.create(BackendService::class.java)
    }

    private fun getLoggingInterceptor(): Interceptor {
        val level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.BASIC
        }
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(level)
        return loggingInterceptor
    }
}
