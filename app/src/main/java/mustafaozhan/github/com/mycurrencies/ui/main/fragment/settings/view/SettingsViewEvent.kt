package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view

import com.github.mustafaozhan.basemob.view.BaseViewEvent
import mustafaozhan.github.com.mycurrencies.model.Currency

interface SettingsViewEvent : BaseViewEvent {
    fun onSelectDeselectButtonsClick(value: Int)

    fun onItemClicked(currency: Currency)
}
