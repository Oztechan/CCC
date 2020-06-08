/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main

import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    preferencesRepository: PreferencesRepository
) : BaseViewModel() {
    val data = MainData(preferencesRepository)
}
