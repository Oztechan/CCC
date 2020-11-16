/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.client

import kotlin.test.Test
import kotlin.test.assertTrue

class GreetingTest {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("iOS"), "Check iOS is mentioned")
    }
}
