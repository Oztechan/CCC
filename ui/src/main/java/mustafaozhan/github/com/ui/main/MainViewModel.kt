/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.ui.main

import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import mustafaozhan.github.com.data.preferences.PreferencesRepository
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    preferencesRepository: PreferencesRepository
) : BaseViewModel() {

    private val _effect = MutableSingleLiveData<MainEffect>()
    val effect: SingleLiveData<MainEffect> = _effect

    val data = MainData(preferencesRepository)

    fun checkRemoteConfig() {
//        _effect.postValue(AppUpdateEffect(it))
    }
}
