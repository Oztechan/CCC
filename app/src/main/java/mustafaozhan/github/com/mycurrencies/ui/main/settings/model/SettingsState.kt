package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency

data class SettingsState(
    val mediator: SettingsStateMediator
) : BaseState() {
    val searchQuery: MutableLiveData<String> = mediator.searchQuery
    val currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData(mutableListOf())
    val noResult: MutableLiveData<Boolean> = MutableLiveData(false)
}

data class SettingsStateMediator(
    val currencyList: MediatorLiveData<MutableList<Currency>> = MediatorLiveData<MutableList<Currency>>(),
    val searchQuery: MediatorLiveData<String> = MediatorLiveData<String>()
)
