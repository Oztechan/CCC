/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.api

class ApiRepository(private val apiFactory: ApiFactory) {
    suspend fun getRatesByBase(base: String) = apiFactory.getRatesByBase(base)
}
