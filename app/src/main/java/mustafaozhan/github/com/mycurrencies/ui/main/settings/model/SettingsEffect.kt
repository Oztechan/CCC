package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import com.github.mustafaozhan.basemob.model.BaseEffect

sealed class SettingsEffect : BaseEffect()

object FewCurrency : SettingsEffect()
