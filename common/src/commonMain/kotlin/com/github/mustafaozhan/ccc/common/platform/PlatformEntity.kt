/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.platform

import com.github.mustafaozhan.ccc.common.model.Platform

data class PlatformEntity(
    val name: String,
    val version: String
)

fun PlatformEntity.toModel() = Platform(name, version)
