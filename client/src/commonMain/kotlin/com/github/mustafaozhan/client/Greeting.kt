/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.client

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}