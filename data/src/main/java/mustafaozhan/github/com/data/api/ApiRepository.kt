/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.data.api

import android.content.Context
import com.github.mustafaozhan.basemob.data.BaseApiRepository
import com.github.mustafaozhan.basemob.error.EmptyParameterException
import com.github.mustafaozhan.data.R
import mustafaozhan.github.com.data.model.Currencies
import mustafaozhan.github.com.data.model.NullBaseException
import javax.inject.Singleton

@Singleton
class ApiRepository(
    var context: Context
) : BaseApiRepository() {

    companion object {
        private const val TIME_OUT: Long = 3
    }

    override val endpoint: String
        get() = context.getString(R.string.backend_endpoint)

    override val timeOut: Long = TIME_OUT

    private val apiService: ApiService by lazy { initApiServices() }

    private fun initApiServices() = createRetrofit(getClient())
        .create(ApiService::class.java)

    suspend fun getRatesByBase(base: String) = apiRequest {
        when {
            base.isEmpty() -> throw EmptyParameterException()
            base == Currencies.NULL.toString() -> throw NullBaseException()
            else -> apiService.getRatesByBase(base)
        }
    }
}
