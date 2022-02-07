/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.backend.util

fun ClassLoader.getResourceByName(
    source: String
) = getResource(source)?.readText() ?: ""
