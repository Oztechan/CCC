/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.di

import com.github.mustafaozhan.ccc.backend.controller.ApiController
import com.github.mustafaozhan.ccc.backend.controller.RootingController
import com.github.mustafaozhan.ccc.common.data.api.ApiRepository
import com.github.mustafaozhan.ccc.common.data.db.OfflineRatesDao
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.di.initCommon

object Koin {
    private val koinApp = initCommon()

    private val apiRepository: ApiRepository by lazy { koinApp.koin.getDependency(ApiRepository::class) }
    private val offlineRatesDao: OfflineRatesDao by lazy {
        koinApp.koin.getDependency(
            OfflineRatesDao::class
        )
    }

    fun getApiController() = ApiController(apiRepository, offlineRatesDao)
    fun getRootingController() = RootingController(offlineRatesDao)
}
