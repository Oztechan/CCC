package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import com.github.mustafaozhan.basemob.model.BaseEvent

sealed class CalculatorEvent : BaseEvent()

object ErrorEvent : CalculatorEvent()

object FewCurrencyEvent : CalculatorEvent()

object ReverseSpinner : CalculatorEvent()

object MaximumInputEvent : CalculatorEvent()

data class OfflineSuccessEvent(val date: String?) : CalculatorEvent()

data class LongClickEvent(val text: String, val name: String) : CalculatorEvent()
