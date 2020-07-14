/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.api

import com.github.mustafaozhan.basemob.data.api.BaseApiRepository
import com.github.mustafaozhan.basemob.error.EmptyParameterException
import com.github.mustafaozhan.data.model.CurrencyType
import com.github.mustafaozhan.data.model.NullBaseException
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
            base == CurrencyType.NULL.toString() -> throw NullBaseException()
            else -> apiFactory.apiService.getRatesByBase(base)
        }
    }
}
