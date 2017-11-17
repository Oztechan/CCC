package mustafaozhan.github.com.mycurrencies.rest

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Mustafa Ozhan on 9/6/17 at 7:45 PM on Arch Linux.
 */
class ApiClient {
    companion object {
        fun get(): Retrofit {
            return Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://api.fixer.io/")
                    .build()
        }
    }
}