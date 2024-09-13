package com.oztechan.ccc.client.viewmodel.selectcurrency.model

import com.oztechan.ccc.common.core.model.Watcher

sealed class SelectCurrencyPurpose {
    data object Base : SelectCurrencyPurpose()
    data class Source(val watcher: Watcher) : SelectCurrencyPurpose()
    data class Target(val watcher: Watcher) : SelectCurrencyPurpose()
}
