package com.oztechan.ccc.android.ui.mobile.content.watchers

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import com.oztechan.ccc.android.ui.mobile.component.SnackViewHost
import com.oztechan.ccc.client.viewmodel.watchers.WatchersEffect
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavHostController.WatchersRootView(
    vm: WatchersViewModel = koinViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = vm.effect) {
        vm.effect.collect {
            Logger.i { "WatchersRootView observeEffects ${it::class.simpleName}" }
            when (it) {
                WatchersEffect.Back -> popBackStack()
                is WatchersEffect.SelectBase -> navigate("select_currency")
                is WatchersEffect.SelectTarget -> navigate("select_currency")
                WatchersEffect.InvalidInput -> snackbarHostState.showSnackbar(it::class.simpleName.orEmpty())
                WatchersEffect.MaximumNumberOfWatchers -> snackbarHostState.showSnackbar(it::class.simpleName.orEmpty())
                WatchersEffect.TooBigInput -> snackbarHostState.showSnackbar(it::class.simpleName.orEmpty())
            }
        }
    }

    SnackViewHost(snackbarHostState) {
        WatchersView(
            state = vm.state.collectAsState(),
            event = vm.event
        )
    }
}
