package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

sealed class ViewEffect

object ErrorEffect : ViewEffect()

object FewCurrencyEffect : ViewEffect()

data class MaximumInputEffect(var input: String) : ViewEffect()

data class OfflineSuccessEffect(val date: String?) : ViewEffect()
