/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.base.BaseRepositoryTest
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Clock

class SettingsRepositoryTest : BaseRepositoryTest<SettingsRepository>() {

    override val repository: SettingsRepository by lazy {
        koin.getDependency(SettingsRepository::class)
    }

    // defaults
    @Test
    fun firstRun() = assertEquals(
        true,
        repository.firstRun
    )

    @Test
    fun currentBase() = assertEquals(
        CurrencyType.NULL.toString(),
        repository.currentBase
    )

    @Test
    fun appTheme() = assertEquals(
        -1,
        repository.appTheme
    )

    @Test
    fun adFreeActivatedDate() = assertEquals(
        0.toLong(),
        repository.adFreeActivatedDate
    )

    @Test
    fun lastReviewRequest() = assertEquals(
        Clock.System.now().toEpochMilliseconds(),
        repository.lastReviewRequest
    )
}
