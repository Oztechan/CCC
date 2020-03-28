package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mustafaozhan.scopemob.castTo
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.extension.gone
import mustafaozhan.github.com.mycurrencies.extension.visible
import mustafaozhan.github.com.mycurrencies.tool.Toasty

@BindingAdapter("settingsViewState")
fun settingsViewState(
    noResultView: TextView,
    settingsViewState: SettingsViewState
) = when (settingsViewState) {
    SettingsViewState.NoResult -> noResultView.visible()
    else -> noResultView.gone()
}

@BindingAdapter("settingsViewState")
fun settingsViewState(
    recyclerView: RecyclerView,
    settingsViewState: SettingsViewState
) = recyclerView.adapter
    ?.castTo<SettingsAdapter>()
    ?.apply {
        when (settingsViewState) {
            SettingsViewState.NoResult -> submitList(mutableListOf())
            is SettingsViewState.Success -> submitList(settingsViewState.currencyList)
        }
    }

@BindingAdapter("settingsViewState")
fun settingsViewState(
    constraintLayout: ConstraintLayout,
    settingsViewState: SettingsViewState
) = when (settingsViewState) {
    SettingsViewState.FewCurrency -> Toasty.showToasty(constraintLayout.context, R.string.choose_at_least_two_currency)
    else -> Unit
}
