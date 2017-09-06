package mustafaozhan.github.com.mycurrencies.rest

import mustafaozhan.github.com.mycurrencies.model.ResponseAll
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mustafa Ozhan on 9/6/17 at 7:45 PM on Arch Linux.
 */

interface ApiInterface {
    @GET("latest")
    fun getByBase(@Query("base") base: String): Call<ResponseAll>

}