/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.ui.main

import androidx.lifecycle.viewModelScope
import com.github.mustafaozhan.basemob.model.MutableSingleLiveData
import com.github.mustafaozhan.basemob.model.SingleLiveData
import com.github.mustafaozhan.basemob.util.toUnit
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mustafaozhan.github.com.data.preferences.PreferencesRepository
import mustafaozhan.github.com.data.remote.RemoteConfigRepository
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    preferencesRepository: PreferencesRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : BaseViewModel() {

    private val _effect = MutableSingleLiveData<MainEffect>()
    val effect: SingleLiveData<MainEffect> = _effect

    val data = MainData(preferencesRepository)

    fun checkRemoteConfig() = viewModelScope.launch {
        remoteConfigRepository.checkRemoteConfig()
            .collect {
                _effect.postValue(AppUpdateEffect(it))
            }
    }.toUnit()
}
