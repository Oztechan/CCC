/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.fake

import com.github.mustafaozhan.ccc.client.repo.SettingsRepository
import com.russhwolf.settings.Settings

@Suppress("TooManyFunctions", "StringLiteralDuplication")
object FakeSettingsRepository : Settings {

    fun getSettingsRepository() = SettingsRepository(this)

    override val keys: Set<String>
        get() = TODO("Not yet implemented")
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBooleanOrNull(key: String): Boolean? {
        TODO("Not yet implemented")
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        TODO("Not yet implemented")
    }

    override fun getDoubleOrNull(key: String): Double? {
        TODO("Not yet implemented")
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        TODO("Not yet implemented")
    }

    override fun getFloatOrNull(key: String): Float? {
        TODO("Not yet implemented")
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return 1
    }

    override fun getIntOrNull(key: String): Int? {
        // todo faked
        TODO("Not yet implemented")
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        // todo faked
        return 1.toLong()
    }

    override fun getLongOrNull(key: String): Long? {
        TODO("Not yet implemented")
    }

    override fun getString(key: String, defaultValue: String): String {
        // todo faked
        return ""
    }

    override fun getStringOrNull(key: String): String? {
        TODO("Not yet implemented")
    }

    override fun hasKey(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun putBoolean(key: String, value: Boolean) {
        TODO("Not yet implemented")
    }

    override fun putDouble(key: String, value: Double) {
        TODO("Not yet implemented")
    }

    override fun putFloat(key: String, value: Float) {
        TODO("Not yet implemented")
    }

    override fun putInt(key: String, value: Int) {
        TODO("Not yet implemented")
    }

    override fun putLong(key: String, value: Long) {
        // todo faked
    }

    override fun putString(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }
}
