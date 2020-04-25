package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import com.github.mustafaozhan.basemob.model.BaseEffect

sealed class CalculatorEffect : BaseEffect()

object ErrorEffect : CalculatorEffect()

object FewCurrencyEffect : CalculatorEffect()

object ReverseSpinner : CalculatorEffect()

object MaximumInputEffect : CalculatorEffect()

// todo BE fix need
// data class OfflineSuccessEffect(val date: String?) : CalculatorEffect()

data class LongClickEffect(val text: String, val name: String) : CalculatorEffect()
