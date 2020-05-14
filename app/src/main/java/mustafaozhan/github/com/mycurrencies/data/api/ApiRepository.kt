/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.api

import com.github.mustafaozhan.basemob.api.BaseApiRepository
import com.github.mustafaozhan.basemob.error.EmptyParameterException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository
@Inject constructor(
    override val apiFactory: ApiFactory
) : BaseApiRepository() {

    suspend fun getRatesByBase(base: String) = apiRequest {
        if (base.isEmpty()) throw EmptyParameterException()
        else apiFactory.apiService.getRatesByBase(base)
    }
}
