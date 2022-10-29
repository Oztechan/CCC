/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("unused")

package com.oztechan.ccc.res

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.getColor
import dev.icerock.moko.resources.getImageByFileName
import platform.UIKit.UIColor
import platform.UIKit.UIScreen

fun getImageByFileName(
    name: String
): ImageResource = MR.images.getImageByFileName(name.toImageFileName()) ?: MR.images.unknown

fun getString(stringResource: StringResource): StringDesc {
    return StringDesc.Resource(stringResource)
}

fun getString(stringResource: StringResource, parameter: Any): StringDesc {
    return StringDesc.ResourceFormatted(stringResource, parameter)
}

fun getColor(colorResource: ColorResource): UIColor {
    return colorResource.getColor(
        UIScreen.mainScreen.traitCollection.userInterfaceStyle
    ).toUIColor()
}
