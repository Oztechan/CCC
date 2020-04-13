package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view

import com.github.mustafaozhan.basemob.model.BaseEvent
import mustafaozhan.github.com.mycurrencies.model.Currency

interface SettingsEvent : BaseEvent {
    fun onSelectDeselectButtonsClick(value: Int)

    fun onItemClick(currency: Currency)
}
