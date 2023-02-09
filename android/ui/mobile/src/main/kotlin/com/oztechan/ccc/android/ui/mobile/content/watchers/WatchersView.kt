package com.oztechan.ccc.android.ui.mobile.content.watchers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.annotations.ThemedPreviews
import com.oztechan.ccc.android.ui.mobile.component.ImageView
import com.oztechan.ccc.android.ui.mobile.component.Preview
import com.oztechan.ccc.android.ui.mobile.component.SnackViewHost
import com.oztechan.ccc.android.ui.mobile.util.toColor
import com.oztechan.ccc.android.ui.mobile.util.toPainter
import com.oztechan.ccc.android.ui.mobile.util.toText
import com.oztechan.ccc.client.viewmodel.watchers.WatchersEffect
import com.oztechan.ccc.client.viewmodel.watchers.WatchersEvent
import com.oztechan.ccc.client.viewmodel.watchers.WatchersState
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import com.oztechan.ccc.common.core.model.Watcher
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavHostController.WatchersView(
    vm: WatchersViewModel = koinViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = vm.effect) {
        vm.effect.collect {
            Logger.i { "WatchersView observeEffects ${it::class.simpleName}" }
            when (it) {
                WatchersEffect.Back -> popBackStack()
                is WatchersEffect.SelectBase -> navigate("select_currency")
                is WatchersEffect.SelectTarget -> navigate("select_currency")
                WatchersEffect.InvalidInput -> snackbarHostState.showSnackbar(it.javaClass.simpleName)
                WatchersEffect.MaximumNumberOfWatchers -> snackbarHostState.showSnackbar(it.javaClass.simpleName)
                WatchersEffect.TooBigNumber -> snackbarHostState.showSnackbar(it.javaClass.simpleName)
            }
        }
    }

    SnackViewHost(snackbarHostState) {
        WatchersViewContent(
            state = vm.state.collectAsState(),
            event = vm.event
        )
    }
}

@Composable
fun WatchersViewContent(
    state: State<WatchersState>,
    event: WatchersEvent
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = R.string.txt_watchers_description.toText(),
            color = R.color.text_weak.toColor(),
            modifier = Modifier
                .background(color = R.color.background_strong.toColor())
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center,
        )

        LazyColumn(Modifier.weight(1f)) {
            items(state.value.watcherList) {
                WatcherItem(
                    watcher = it,
                    event = event
                )
            }
        }

        Box(
            Modifier
                .background(color = R.color.background_strong.toColor())
                .fillMaxWidth()
                .height(75.dp),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = event::onAddClick,
            ) {
                ImageView(
                    painter = R.drawable.ic_plus.toPainter(),
                    modifier = Modifier.padding(2.dp)
                )

                Text(
                    text = R.string.txt_add.toText(),
                    modifier = Modifier.padding(2.dp),
                    color = R.color.text.toColor(),
                )
            }
        }
    }
}

@Suppress("UnrememberedMutableState")
@Composable
@ThemedPreviews
fun WatchersViewContentPreview() = Preview {
    WatchersViewContent(
        state = mutableStateOf(
            WatchersState(
                watcherList = listOf(
                    Watcher(id = 0, base = "EUR", target = "USD", isGreater = false, rate = 123.0),
                    Watcher(id = 0, base = "USD", target = "EUR", isGreater = false, rate = 123.0)
                )
            )
        ),
        event = object : WatchersEvent {
            override fun onBackClick() = Unit
            override fun onBaseClick(watcher: Watcher) = Unit
            override fun onTargetClick(watcher: Watcher) = Unit
            override fun onBaseChanged(watcher: Watcher, newBase: String) = Unit
            override fun onTargetChanged(watcher: Watcher, newTarget: String) = Unit
            override fun onAddClick() = Unit
            override fun onDeleteClick(watcher: Watcher) = Unit
            override fun onRelationChange(watcher: Watcher, isGreater: Boolean) = Unit
            override fun onRateChange(watcher: Watcher, rate: String) = ""
        }
    )
}
