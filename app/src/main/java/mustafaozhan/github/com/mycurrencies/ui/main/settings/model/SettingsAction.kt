package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import com.github.mustafaozhan.basemob.model.BaseAction
import mustafaozhan.github.com.mycurrencies.model.Currency

interface SettingsAction : BaseAction {
    fun onSelectDeselectButtonsClick(value: Int)

    fun onItemClick(currency: Currency)
}
