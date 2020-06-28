/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main

import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import javax.inject.Inject

class MainViewModel
@Inject constructor(preferencesRepository: PreferencesRepository) : BaseViewModel() {
    val data = MainData(preferencesRepository)
}
