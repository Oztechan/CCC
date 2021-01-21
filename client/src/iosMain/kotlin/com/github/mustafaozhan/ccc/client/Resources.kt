/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.getColor
import platform.UIKit.UIColor
import platform.UIKit.UIScreen

fun getColor(colorResource: ColorResource): UIColor {
    return colorResource.getColor(
        UIScreen.mainScreen.traitCollection.userInterfaceStyle
    ).toUIColor()
}
