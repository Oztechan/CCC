/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformRepositoryTest : BaseRepositoryTest() {
    @Test
    fun checkIOSIsMentioned() {
        assertTrue(
            platformRepository.platform.name.contains("iOS"),
            "Check iOS is mentioned"
        )
    }
}
