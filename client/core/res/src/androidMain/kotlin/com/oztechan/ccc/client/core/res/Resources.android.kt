package com.oztechan.ccc.client.core.res

private val drawableIdsByName: Map<String, Int> by lazy {
    R.drawable::class.java.fields.associate { it.name to it.getInt(null) }
}

/**
 * Resolves a currency flag from the generated R class rather than through moko's
 * `getImageByFileName`.
 *
 * moko-resources 0.27 generates the accessors as top level extension properties, so they compile
 * to static methods on a file facade. That helper still reflects over the instance methods of
 * `Res.images`, finds nothing, and every flag falls back to the unknown placeholder. Going through
 * R also drops the dependency on moko's generated shape, which has broken this lookup before - see
 * icerockdev/moko-resources#207.
 */
fun String.getImageIdByName(): Int = drawableIdsByName[toImageFileName()] ?: R.drawable.unknown
