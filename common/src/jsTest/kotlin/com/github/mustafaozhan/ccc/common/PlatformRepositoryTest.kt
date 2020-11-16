/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformRepositoryTest : BaseRepositoryTest() {
    @Test
    fun checkJsIsMentioned() {
        assertTrue(
            platformRepository.platform.name.contains("JS"),
            "Check JS is mentioned"
        )
    }
}
