package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view

import com.github.mustafaozhan.basemob.view.BaseViewEvent

interface SettingsViewEvent : BaseViewEvent {
    fun onSelectDeselectButtonsClick(value: Int)

    fun onItemClicked(value: Int, name: String)
}
