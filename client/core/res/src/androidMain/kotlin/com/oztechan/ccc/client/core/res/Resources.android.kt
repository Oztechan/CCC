package com.oztechan.ccc.client.core.res

/**
 * moko-resources 0.27 generates the accessors as top level extension properties, which its own
 * `getImageByFileName` cannot find: that helper reflects over the instance methods of
 * `Res.images`, and the accessors are now static methods on a file facade. Every lookup missed and
 * fell back to the unknown placeholder, so no currency flag was drawn.
 *
 * The generated R class still names every drawable, so resolve against it directly. That also
 * drops the dependency on moko's generated shape, which has broken this lookup twice already -
 * see icerockdev/moko-resources#207.
 */
private val drawableIdsByName: Map<String, Int> by lazy {
    R.drawable::class.java.fields.associate { it.name to it.getInt(null) }
}

fun String.getImageIdByName(): Int = drawableIdsByName[toImageFileName()] ?: R.drawable.unknown
