package mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mustafaozhan.scopemob.castTo
import com.wang.avi.AVLoadingIndicatorView
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.extension.gone
import mustafaozhan.github.com.mycurrencies.extension.visible
import mustafaozhan.github.com.mycurrencies.tool.Toasty
import mustafaozhan.github.com.mycurrencies.tool.showSnacky

@BindingAdapter("calculatorViewState")
fun calculatorViewState(
    emptyView: TextView,
    calculatorViewState: CalculatorViewState
) = when (calculatorViewState) {
    is CalculatorViewState.Empty -> emptyView.visible()
    else -> emptyView.gone()
}

@BindingAdapter("calculatorViewState")
fun calculatorViewState(
    loadingView: AVLoadingIndicatorView,
    calculatorViewState: CalculatorViewState
) = when (calculatorViewState) {
    CalculatorViewState.Loading -> loadingView.smoothToShow()
    else -> loadingView.smoothToHide()
}

@BindingAdapter("calculatorViewState")
fun calculatorViewState(
    constraintLayout: ConstraintLayout,
    calculatorViewState: CalculatorViewState
) = when (calculatorViewState) {
    CalculatorViewState.MaximumInput -> Toasty.showToasty(constraintLayout.context, R.string.max_input)
    else -> Unit
}

@BindingAdapter("calculatorViewState")
fun calculatorViewState(
    recyclerView: RecyclerView,
    calculatorViewState: CalculatorViewState
) = recyclerView.adapter
    ?.castTo<CalculatorAdapter>()
    ?.apply {
        when (calculatorViewState) {
            is CalculatorViewState.Success ->
                refreshList(
                    calculatorViewState.currencyList,
                    calculatorViewState.baseCurrency
                )
            is CalculatorViewState.OfflineSuccess -> {
                refreshList(
                    calculatorViewState.currencyList,
                    calculatorViewState.baseCurrency
                )
                calculatorViewState.rates.date?.let {
                    Toasty.showToasty(
                        recyclerView.context,
                        recyclerView.context.getString(R.string.database_success_with_date, it)
                    )
                } ?: run {
                    Toasty.showToasty(recyclerView.context, R.string.database_success)
                }
            }
            is CalculatorViewState.Error -> {
                if (calculatorViewState.isFewCurrency) {
                    showSnacky(
                        recyclerView,
                        R.string.rate_not_available_offline,
//                        R.string.change, todo isIndefinite to true after solving here
                        isIndefinite = false
                    )
//                    { binding.layoutBar.spinnerBase.expand() } todo
                }
                refreshList(mutableListOf())
            }
            CalculatorViewState.FewCurrency -> {
                showSnacky(recyclerView, R.string.choose_at_least_two_currency, R.string.select)
//                { todo
//                    replaceFragment(SettingsFragment.newInstance(), true)
//                }
                refreshList(mutableListOf())
            }
            CalculatorViewState.Empty -> refreshList(mutableListOf())
        }
    }
