/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.splash

import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import javax.inject.Inject

class SplashViewModel
@Inject constructor(
    val preferencesRepository: PreferencesRepository
) : BaseViewModel()
