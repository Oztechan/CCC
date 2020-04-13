package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view

import com.github.mustafaozhan.basemob.model.BaseEffect

sealed class SettingsEffect : BaseEffect()

object FewCurrency : SettingsEffect()
