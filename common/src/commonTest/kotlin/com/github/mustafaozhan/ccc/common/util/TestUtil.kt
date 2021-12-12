package com.github.mustafaozhan.ccc.common.util

import kotlin.test.assertTrue

fun assertAllTrue(vararg condition: Boolean) {
    condition.forEach {
        assertTrue { it }
    }
}
