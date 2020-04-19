package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency

@Suppress("ConstructorParameterNaming")
data class SettingsState(
    private val _state: _SettingsState
) : BaseState() {
    val currencyList: LiveData<MutableList<Currency>> = _state._currencyList

    // two way binding
    val searchQuery: MutableLiveData<String> = _state._searchQuery
}

@Suppress("ClassNaming", "ClassName", "ConstructorParameterNaming")
data class _SettingsState(
    val _currencyList: MediatorLiveData<MutableList<Currency>> = MediatorLiveData<MutableList<Currency>>(),
    val _searchQuery: MediatorLiveData<String> = MediatorLiveData<String>()
)
