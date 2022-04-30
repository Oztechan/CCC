package com.oztechan.ccc.resources

import android.widget.ImageView

fun getDrawableIdByFileName(
    name: String
): Int = getImageByFileName(name.toImageFileName()).drawableResId

fun ImageView.setBackgroundByName(
    name: String
) = setImageResource(getDrawableIdByFileName(name))

