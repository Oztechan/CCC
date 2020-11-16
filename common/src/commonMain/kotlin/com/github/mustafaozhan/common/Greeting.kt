/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.common

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}