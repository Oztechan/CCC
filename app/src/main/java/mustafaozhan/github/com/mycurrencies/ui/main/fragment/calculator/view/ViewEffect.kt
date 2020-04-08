package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

sealed class ViewEffect

object ErrorEffect : ViewEffect()

object FewCurrencyEffect : ViewEffect()

data class MaximumInputEffect(var input: String) : ViewEffect()

data class OfflineSuccessEffect(val date: String?) : ViewEffect()

data class LongClickEffect(val text: String, val name: String) : ViewEffect()

data class SwitchBaseEffect(val text: String, val base: String, val index: Int) : ViewEffect()
