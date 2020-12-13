/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import com.github.mustafaozhan.ccc.common.di.initKoin
import com.github.mustafaozhan.ccc.common.platform.PlatformRepository
import kotlin.test.BeforeTest
import org.koin.core.KoinApplication

open class BaseRepositoryTest {
    private lateinit var application: KoinApplication
    protected lateinit var platformRepository: PlatformRepository

    @BeforeTest
    fun init() {
        application = initKoin()
        platformRepository = application.koin.get(PlatformRepository::class)
    }
}
