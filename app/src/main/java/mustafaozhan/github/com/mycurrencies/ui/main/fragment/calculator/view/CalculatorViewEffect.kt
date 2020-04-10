package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import com.github.mustafaozhan.basemob.view.BaseViewEffect

sealed class CalculatorViewEffect : BaseViewEffect()

object ErrorEffect : CalculatorViewEffect()

object FewCurrencyEffect : CalculatorViewEffect()

data class MaximumInputEffect(var input: String) : CalculatorViewEffect()

data class OfflineSuccessEffect(val date: String?) : CalculatorViewEffect()

data class LongClickEffect(val text: String, val name: String) : CalculatorViewEffect()

data class SwitchBaseEffect(val text: String, val base: String, val index: Int) : CalculatorViewEffect()
