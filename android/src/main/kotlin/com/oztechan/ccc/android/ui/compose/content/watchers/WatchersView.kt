package com.oztechan.ccc.android.ui.compose.content.watchers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.oztechan.ccc.android.R
import com.oztechan.ccc.android.ui.compose.annotations.ProjectPreviews
import com.oztechan.ccc.android.ui.compose.component.ImageView
import com.oztechan.ccc.android.ui.compose.component.Preview
import com.oztechan.ccc.client.model.Watcher
import com.oztechan.ccc.client.viewmodel.watchers.WatchersEvent
import com.oztechan.ccc.client.viewmodel.watchers.WatchersState
import com.oztechan.ccc.client.viewmodel.watchers.WatchersViewModel
import org.koin.androidx.compose.koinViewModel

@Suppress("FunctionNaming")
@Composable
fun WatchersView(
    vm: WatchersViewModel = koinViewModel(),
) {
    WatchersViewContent(
        state = vm.state.collectAsState(),
        event = vm.event
    )
}

@Suppress("FunctionNaming")
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
            text = stringResource(id = R.string.txt_watchers_description),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(Modifier.weight(1f)) {
            items(state.value.watcherList) {
                WatcherItem(watcher = it) { rate ->
                    event.onRateChange(it, rate)
                }
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .height(75.dp),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = event::onAddClick,
            ) {
                ImageView(
                    painter = painterResource(id = R.drawable.ic_plus),
                    modifier = Modifier.padding(2.dp)
                )

                Text(
                    text = stringResource(R.string.txt_add),
                    modifier = Modifier.padding(2.dp),
                    color = colorResource(id = R.color.text),
                )
            }
        }
    }
}

@Suppress("FunctionNaming", "UnrememberedMutableState")
@Composable
@ProjectPreviews
fun WatchersViewContentPreview() = Preview {
    WatchersViewContent(
        state = mutableStateOf(
            WatchersState(
                watcherList = listOf(
                    Watcher(id = 0, base = "EUR", target = "USD", isGreater = false, rate = "123"),
                    Watcher(id = 0, base = "USD", target = "EUR", isGreater = false, rate = "123")
                ),
                base = "",
                target = ""
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
            override fun onRateChange(watcher: Watcher, rate: String): String = ""
        }
    )
}
