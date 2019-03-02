package mustafaozhan.github.com.mycurrencies.base.api.exchangerates

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
class ExchangeRatesApiHelper @Inject
constructor() : BaseApiHelper() {

    companion object {
        const val TIME_OUT: Long = 500
    }

    val exchangeRatesApiServices: ExchangeRatesApiServices by lazy { initExchangeRatesApiServices() }

    private fun initExchangeRatesApiServices(): ExchangeRatesApiServices {
        val clientBuilder = OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
        clientBuilder.addInterceptor {
            it.proceed(createInterceptorRequest(it))
        }
        val endpoint = getString(R.string.exchange_rates_endpoint)
        val retrofit = initRxRetrofit(endpoint, clientBuilder.build())
        return retrofit.create(ExchangeRatesApiServices::class.java)
    }

    private fun createInterceptorRequest(chain: Interceptor.Chain): Request {
        val original = chain.request()
        val builder = original.newBuilder()
        return builder.build()
    }
}