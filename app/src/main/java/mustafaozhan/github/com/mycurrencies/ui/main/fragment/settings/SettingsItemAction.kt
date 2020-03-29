package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import com.github.mustafaozhan.basemob.action.BaseItemAction
import mustafaozhan.github.com.mycurrencies.databinding.ItemSettingBinding
import mustafaozhan.github.com.mycurrencies.model.Currency

interface SettingsItemAction : BaseItemAction {
    fun onSettingsItemClick(itemSettingBinding: ItemSettingBinding, currency: Currency)
}
