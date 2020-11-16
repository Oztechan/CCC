/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.client

import org.junit.Assert.assertTrue
import org.junit.Test

class GreetingTest {

    @Test
    fun testExample() {
        assertTrue("Check Android is mentioned", Greeting().greeting().contains("Android"))
    }
}
