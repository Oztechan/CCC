/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.fake

import com.russhwolf.settings.Settings

@Suppress("TooManyFunctions", "StringLiteralDuplication")
object FakeSettings : Settings {

    fun getSettings() = this as Settings

    override val keys: Set<String>
        get() = TODO("Not yet implemented")
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun clear() = Unit

    override fun getBoolean(key: String, defaultValue: Boolean) = defaultValue

    override fun getBooleanOrNull(key: String): Boolean? = null

    override fun getDouble(key: String, defaultValue: Double) = defaultValue

    override fun getDoubleOrNull(key: String): Double? = null

    override fun getFloat(key: String, defaultValue: Float) = defaultValue

    override fun getFloatOrNull(key: String): Float? = null

    override fun getInt(key: String, defaultValue: Int) = defaultValue

    override fun getIntOrNull(key: String): Int? = null

    override fun getLong(key: String, defaultValue: Long) = defaultValue

    override fun getLongOrNull(key: String): Long? = null

    override fun getString(key: String, defaultValue: String) = defaultValue

    override fun getStringOrNull(key: String): String? = null

    override fun hasKey(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun putBoolean(key: String, value: Boolean) = Unit

    override fun putDouble(key: String, value: Double) = Unit

    override fun putFloat(key: String, value: Float) = Unit

    override fun putInt(key: String, value: Int) = Unit

    override fun putLong(key: String, value: Long) = Unit

    override fun putString(key: String, value: String) = Unit

    override fun remove(key: String) = Unit
}
