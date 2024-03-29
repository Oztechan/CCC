/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("unused")

package com.oztechan.ccc.client.core.res

import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.getUIColor

fun getString(stringResource: StringResource): StringDesc {
    return StringDesc.Resource(stringResource)
}

fun getString(stringResource: StringResource, parameter: Any): StringDesc {
    return StringDesc.ResourceFormatted(stringResource, parameter)
}

fun getColor(colorResource: ColorResource) = colorResource.getUIColor()
