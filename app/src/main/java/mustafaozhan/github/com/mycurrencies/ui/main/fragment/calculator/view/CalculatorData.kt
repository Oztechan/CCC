package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.view

import com.github.mustafaozhan.basemob.model.BaseData
import mustafaozhan.github.com.mycurrencies.model.Rates

data class CalculatorData(
    var rates: Rates? = null
) : BaseData()
