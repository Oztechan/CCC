package mustafaozhan.github.com.mycurrencies.base.api

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:44 PM on Arch Linux wit Love <3.
 */
abstract class BaseApiHelper {

    protected abstract val moshi: Moshi

    protected fun initRxRetrofit(endpoint: String, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(endpoint)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}
