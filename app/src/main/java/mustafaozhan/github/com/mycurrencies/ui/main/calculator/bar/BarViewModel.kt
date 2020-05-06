/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator.bar

import com.github.mustafaozhan.basemob.lifecycle.MutableSingleLiveData
import com.github.mustafaozhan.basemob.lifecycle.SingleLiveData
import com.github.mustafaozhan.basemob.viewmodel.BaseViewModel
import mustafaozhan.github.com.mycurrencies.model.Currency

class BarViewModel : BaseViewModel(), BarEvent {
    // region SEED
    private val _state = BarStateBacking()
    val state = BarState(_state)

    private val _effect = MutableSingleLiveData<BarEffect>()
    val effect: SingleLiveData<BarEffect> = _effect

    val data = BarData()

    fun getEvent() = this as BarEvent
    // endregion

    // region Event
    override fun onItemClick(currency: Currency) = Unit
    // endregion
}
