/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.repo

import com.russhwolf.settings.Settings

class SettingsRepository(private val settings: Settings) {
    companion object {
        const val SETTINGS_NAME = "ccc_settings"
        private const val KEY_FIRST_RUN = "key_run_counter"
    }

    var runCounter
        get() = settings.getInt(KEY_FIRST_RUN, 0)
        set(value) = settings.putInt(KEY_FIRST_RUN, value)
}
