/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.settings

import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel

class SettingsViewModel : BaseViewModel(), SettingsEvent {
    fun getEvent() = this as SettingsEvent
}
