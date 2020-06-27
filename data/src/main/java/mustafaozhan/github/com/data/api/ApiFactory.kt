/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.data.api

import android.content.Context
import com.github.mustafaozhan.basemob.data.api.BaseApiFactory
import com.github.mustafaozhan.data.R
import javax.inject.Inject

class ApiFactory
@Inject constructor(
    private val context: Context
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
