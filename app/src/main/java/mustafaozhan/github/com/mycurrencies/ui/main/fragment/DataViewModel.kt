package mustafaozhan.github.com.mycurrencies.ui.main.fragment

import com.github.mustafaozhan.basemob.model.BaseData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import com.github.mustafaozhan.basemob.viewmodel.SEEDViewModel
import mustafaozhan.github.com.mycurrencies.data.preferences.PreferencesRepository
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.model.MainData
import mustafaozhan.github.com.mycurrencies.tool.enumValueOrNull
import org.joda.time.Duration
import org.joda.time.Instant

abstract class DataViewModel
<State : BaseState, Event : BaseEvent, Effect : BaseEffect, Data : BaseData>(
    protected var preferencesRepository: PreferencesRepository
) : SEEDViewModel<State, Event, Effect, Data>() {

    companion object {
        const val NUMBER_OF_HOURS = 24
        const val MINIMUM_ACTIVE_CURRENCY = 2
    }

    val mainDataViewState = DataState(DataViewStateObserver())

    init {
        mainDataViewState.base.value = mainData.currentBase.toString()

        mainDataViewState.observer.base.addSource(mainDataViewState.base) {
            setCurrentBase(it)
            currentBaseChanged(it)
        }
    }

    abstract fun currentBaseChanged(newBase: String)

    internal val mainData: MainData
        get() = preferencesRepository.loadMainData()

    val isRewardExpired: Boolean
        get() = preferencesRepository.loadMainData().adFreeActivatedDate?.let {
            Duration(it, Instant.now()).standardHours > NUMBER_OF_HOURS
        } ?: true

    internal fun setCurrentBase(newBase: String?) = preferencesRepository
        .updateMainData(currentBase = enumValueOrNull<Currencies>(newBase ?: "NULL"))
}
