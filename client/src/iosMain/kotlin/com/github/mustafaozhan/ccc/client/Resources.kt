/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("unused")

package com.github.mustafaozhan.ccc.client

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Plural
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.getColor
import dev.icerock.moko.resources.getImageByFileName
import platform.UIKit.UIColor
import platform.UIKit.UIScreen

fun getDrawableByFileName(name: String): ImageResource {
    return MR.images.getImageByFileName(
        name.toLowerCase().replace("try", "tryy")
    ) ?: MR.images.transparent
}

fun getString(stringResource: StringResource): StringDesc {
    return StringDesc.Resource(stringResource)
}

fun getString(stringResource: PluralsResource, quantity: Int): StringDesc {
    return StringDesc.Plural(stringResource, quantity)
}

fun getColor(colorResource: ColorResource): UIColor {
    return colorResource.getColor(
        UIScreen.mainScreen.traitCollection.userInterfaceStyle
    ).toUIColor()
}
