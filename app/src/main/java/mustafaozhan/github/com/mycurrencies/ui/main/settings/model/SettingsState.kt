package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency

data class SettingsState(
    private val backing: SettingsStateBacking
) : BaseState() {
    val currencyList: LiveData<MutableList<Currency>> = backing._currencyList

    // two way binding
    val searchQuery: LiveData<String> = backing._searchQuery
}

@Suppress("ConstructorParameterNaming")
data class SettingsStateBacking(
    val _currencyList: MediatorLiveData<MutableList<Currency>> = MediatorLiveData<MutableList<Currency>>(),
    val _searchQuery: MediatorLiveData<String> = MediatorLiveData<String>()
)
