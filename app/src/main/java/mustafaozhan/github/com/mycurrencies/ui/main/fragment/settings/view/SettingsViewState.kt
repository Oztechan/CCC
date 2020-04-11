package mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.view

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.view.BaseViewState
import mustafaozhan.github.com.mycurrencies.model.Currency

data class SettingsViewStateWrapper(
    val searchQuery: MediatorLiveData<String> = MediatorLiveData<String>()
)

data class SettingsViewState(val wrapper: SettingsViewStateWrapper) : BaseViewState() {
    val searchQuery: MutableLiveData<String> = wrapper.searchQuery
    val currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData(mutableListOf())
    val noResult: MutableLiveData<Boolean> = MutableLiveData(false)
}
