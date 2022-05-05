package com.oztechan.ccc.resources

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.getImageByFileName

fun getImageByFileName(
    name: String
): ImageResource = MR.images.getImageByFileName(name.toImageFileName()) ?: MR.images.unknown

fun String.toImageFileName() = lowercase()
    .replace("try", "tryy")
    .ifEmpty { "unknown" }
