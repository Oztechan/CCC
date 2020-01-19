package mustafaozhan.github.com.mycurrencies.data.backend

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.api.BaseApiHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
@Singleton
class BackendHelper @Inject
constructor() : BaseApiHelper() {

    companion object {
        const val SIMPLE_YYYY_MM_DD = "yyyy-MM-dd'T'HH:mm:ss"
    }

    override val gSon: Gson
        get() = GsonBuilder()
            .setDateFormat(SIMPLE_YYYY_MM_DD)
            .create()

    val backendService: BackendService by lazy { initBackendApiServices() }

    private fun initBackendApiServices(): BackendService {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1L, TimeUnit.SECONDS)
            .writeTimeout(1L, TimeUnit.SECONDS)
        clientBuilder.addInterceptor {
            it.proceed(createInterceptorRequest(it))
        }
        val endpoint = getString(R.string.backend_endpoint)
        val retrofit = initRxRetrofit(endpoint, clientBuilder.build())
        return retrofit.create(BackendService::class.java)
    }

    private fun createInterceptorRequest(chain: Interceptor.Chain): Request {
        val original = chain.request()
        val builder = original.newBuilder()
        return builder.build()
    }
}
