package mustafaozhan.github.com.mycurrencies.ui.main.calculator.model

import com.github.mustafaozhan.basemob.model.BaseData
import mustafaozhan.github.com.mycurrencies.model.Rates

data class CalculatorData(
    var rates: Rates? = null
) : BaseData()
