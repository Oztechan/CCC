package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import com.github.mustafaozhan.basemob.model.BaseEvent

sealed class SettingsEvent : BaseEvent()

object FewCurrencyEvent : SettingsEvent()
