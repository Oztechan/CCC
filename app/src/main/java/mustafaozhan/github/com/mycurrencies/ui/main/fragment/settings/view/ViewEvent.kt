package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view

interface ViewEvent {
    fun updateAllStates(value: Int)

    fun updateCurrencyState(value: Int, name: String)
}
