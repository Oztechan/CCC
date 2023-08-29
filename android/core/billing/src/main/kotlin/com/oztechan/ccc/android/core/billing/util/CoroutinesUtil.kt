package com.oztechan.ccc.android.core.billing.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

internal fun LifecycleOwner.launchWithLifeCycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend () -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            block()
        }
    }
}
