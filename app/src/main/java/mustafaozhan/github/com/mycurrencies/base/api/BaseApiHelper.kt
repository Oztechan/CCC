package mustafaozhan.github.com.mycurrencies.base.api

import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import mustafaozhan.github.com.mycurrencies.app.CCCApplication
import mustafaozhan.github.com.mycurrencies.base.api.exception.RxErrorHandlingCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:44 PM on Arch Linux wit Love <3.
 */
abstract class BaseApiHelper {

    abstract val gSon: Gson

    protected fun getString(resId: Int): String {
        return CCCApplication.instance.getString(resId)
    }

    protected fun initRxRetrofit(endpoint: String, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(endpoint)
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .client(httpClient)
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(Schedulers.io()))
            .build()
    }
}
