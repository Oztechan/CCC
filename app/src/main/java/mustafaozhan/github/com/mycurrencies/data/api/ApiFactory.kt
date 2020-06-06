/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.api

import android.content.Context
import com.github.mustafaozhan.basemob.data.api.BaseApiFactory
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.di.ApplicationContext
import javax.inject.Inject

class ApiFactory
@Inject constructor(
    @ApplicationContext private val context: Context
) : BaseApiFactory() {

    companion object {
        private const val TIME_OUT: Long = 3
    }

    override val endpoint: String
        get() = context.getString(R.string.backend_endpoint)

    override val timeOut: Long = TIME_OUT

    val apiService: ApiService by lazy { initApiServices() }

    private fun initApiServices() = createRetrofit(getClient())
        .create(ApiService::class.java)
}
