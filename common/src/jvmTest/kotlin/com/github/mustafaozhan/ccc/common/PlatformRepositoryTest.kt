/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import org.junit.Assert.assertTrue
import org.junit.Test

class PlatformRepositoryTest : BaseRepositoryTest() {
    @Test
    fun checkJvmIsMentioned() {
        assertTrue(
            "Check JVM is mentioned",
            platformRepository.platform.name.contains("JVM")
        )
    }
}
