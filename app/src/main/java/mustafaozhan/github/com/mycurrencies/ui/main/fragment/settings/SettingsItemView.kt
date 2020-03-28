package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import com.github.mustafaozhan.basemob.view.BaseItemView
import mustafaozhan.github.com.mycurrencies.databinding.ItemSettingBinding
import mustafaozhan.github.com.mycurrencies.model.Currency

interface SettingsItemView : BaseItemView {
    fun onSettingsItemClick(itemSettingBinding: ItemSettingBinding, currency: Currency)
}
