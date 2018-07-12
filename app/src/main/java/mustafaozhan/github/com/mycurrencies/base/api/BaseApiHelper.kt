package mustafaozhan.github.com.mycurrencies.base.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import mustafaozhan.github.com.mycurrencies.application.Application
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:44 PM on Arch Linux wit Love <3.
 */
abstract class BaseApiHelper {

    companion object {
        const val SIMPLE_YYYY_MM_DD = "yyyy-MM-dd'T'HH:mm:ss"
    }

    protected val gson: Gson
        get() = GsonBuilder().create()


    protected fun getString(resId: Int): String {
        return Application.instance.getString(resId)
    }

    protected fun initRxRetrofit(endpoint: String, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(Schedulers.io()))
                .build()
    }
}