package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import com.github.mustafaozhan.basemob.model.BaseEffect

sealed class CalculatorEffect : BaseEffect()

object ErrorEffect : CalculatorEffect()

object FewCurrencyEffect : CalculatorEffect()

object ReverseSpinner : CalculatorEffect()

data class MaximumInputEffect(var input: String) : CalculatorEffect()

data class OfflineSuccessEffect(val date: String?) : CalculatorEffect()

data class LongClickEffect(val text: String, val name: String) : CalculatorEffect()
