package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import mustafaozhan.github.com.mycurrencies.model.Currency

interface ViewEvent {

    fun onRowClick(currency: Currency)

    fun onRowLongClick(currency: Currency): Boolean
}
