/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.bookbarter.common

import com.github.mustafaozhan.bookbarter.common.di.initKoin
import com.github.mustafaozhan.bookbarter.common.repository.PlatformRepository
import org.koin.core.KoinApplication
import kotlin.test.BeforeTest

open class BaseRepositoryTest {
    private lateinit var application: KoinApplication
    protected lateinit var platformRepository: PlatformRepository

    @BeforeTest
    fun init() {
        application = initKoin()
        platformRepository = application.koin.get(PlatformRepository::class)
    }
}
