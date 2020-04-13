package mustafaozhan.github.com.mycurrencies.ui.main.settings.model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseState
import mustafaozhan.github.com.mycurrencies.model.Currency

data class SettingsState(
    val observer: SettingsStateObserver
) : BaseState() {
    val searchQuery: MutableLiveData<String> = observer.searchQuery
    val currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData(mutableListOf())
    val noResult: MutableLiveData<Boolean> = MutableLiveData(false)
}

data class SettingsStateObserver(
    val currencyList: MediatorLiveData<MutableList<Currency>> = MediatorLiveData<MutableList<Currency>>(),
    val searchQuery: MediatorLiveData<String> = MediatorLiveData<String>()
)
