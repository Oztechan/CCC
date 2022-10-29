/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.util

internal fun ClassLoader.getResourceByName(
    source: String
) = getResource(source)?.readText().orEmpty()
