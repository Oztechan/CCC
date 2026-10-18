package com.oztechan.ccc.client.core.res

private val drawableIdsByName: Map<String, Int> by lazy {
    R.drawable::class.java.fields.associate { it.name to it.getInt(null) }
}

// moko 0.27 moved the accessors to extension properties, so its getImageByFileName
// finds nothing and every flag falls back to unknown (Oztechan/CCC#4922).
fun String.getImageIdByName(): Int = drawableIdsByName[toImageFileName()] ?: R.drawable.unknown
