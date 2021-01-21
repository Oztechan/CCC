/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.getColor
import platform.UIKit.UIColor
import platform.UIKit.UIUserInterfaceStyle

fun getColor(
    colorResource: ColorResource,
    isDark: Boolean
): UIColor {
    return colorResource.getColor(
        if (isDark) UIUserInterfaceStyle.UIUserInterfaceStyleDark
        else UIUserInterfaceStyle.UIUserInterfaceStyleLight
    ).toUIColor()
}
