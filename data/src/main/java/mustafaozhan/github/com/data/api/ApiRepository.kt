/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.data.api

import com.github.mustafaozhan.basemob.data.api.BaseApiRepository
import com.github.mustafaozhan.basemob.error.EmptyParameterException
import mustafaozhan.github.com.data.model.Currencies
import mustafaozhan.github.com.data.model.NullBaseException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository
@Inject constructor(
    override val apiFactory: ApiFactory
) : BaseApiRepository() {

    suspend fun getRatesByBase(base: String) = apiRequest {
        when {
            base.isEmpty() -> throw EmptyParameterException()
            base == Currencies.NULL.toString() -> throw NullBaseException()
            else -> apiFactory.apiService.getRatesByBase(base)
        }
    }
}
