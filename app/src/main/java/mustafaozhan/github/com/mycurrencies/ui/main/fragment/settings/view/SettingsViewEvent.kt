package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view

import com.github.mustafaozhan.basemob.view.BaseViewEvent

interface SettingsViewEvent : BaseViewEvent {
    fun updateAllStates(value: Int)

    fun updateCurrencyState(value: Int, name: String)
}
