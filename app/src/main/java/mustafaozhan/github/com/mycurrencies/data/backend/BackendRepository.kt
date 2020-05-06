/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.backend

import com.github.mustafaozhan.basemob.api.BaseApiRepository
import com.github.mustafaozhan.basemob.error.EmptyParameterException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackendRepository
@Inject constructor(
    override val apiHelper: BackendHelper
) : BaseApiRepository() {

    suspend fun getRatesByBase(base: String) = apiRequest {
        if (base.isEmpty()) throw EmptyParameterException()
        else apiHelper.backendService.getRatesByBase(base)
    }

    suspend fun getRatesByBaseLongTimeOut(base: String) = apiRequest {
        if (base.isEmpty()) throw EmptyParameterException()
        else apiHelper.backendServiceLongTimeOut.getRatesByBase(base)
    }
}
