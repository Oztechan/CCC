@file:OptIn(ExperimentalMaterial3Api::class)

package com.oztechan.ccc.android.ui.mobile.content.watchers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.annotations.ThemedPreviews
import com.oztechan.ccc.android.ui.mobile.component.ImageView
import com.oztechan.ccc.android.ui.mobile.component.Preview
import com.oztechan.ccc.android.ui.mobile.util.toPainter
import com.oztechan.ccc.android.ui.mobile.util.toText
import com.oztechan.ccc.client.core.res.getImageIdByName
import com.oztechan.ccc.client.viewmodel.watchers.WatchersEvent
import com.oztechan.ccc.common.core.model.Watcher

@Composable
fun WatcherItem(
    watcher: Watcher,
    event: WatchersEvent
) {
    val itemPadding: Dp = 2.dp
    val itemHeight: Dp = 36.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = R.string.one.toText(),
            modifier = Modifier.padding(itemPadding),
        )

        Text(
            text = watcher.base,
            modifier = Modifier.padding(itemPadding),
        )

        ImageView(
            painter = watcher.base.getImageIdByName().toPainter(),
            modifier = Modifier
                .size(itemHeight)
                .padding(itemPadding)
                .clickable { event.onBaseClick(watcher) }
        )

        Text(
            text = "is greater than",
            modifier = Modifier.padding(itemPadding),
        )

        Spacer(Modifier.weight(1f))

        OutlinedTextField(
            value = watcher.rate.toString(),
            onValueChange = { event.onRateChange(watcher, it) },
            modifier = Modifier
                .padding(itemPadding)
                .width(105.dp),
            placeholder = {
                Text(text = R.string.txt_rate.toText())
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = TextStyle(textAlign = TextAlign.Center)
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = watcher.target,
            modifier = Modifier.padding(itemPadding),
        )

        ImageView(
            painter = watcher.target.getImageIdByName().toPainter(),
            modifier = Modifier
                .size(itemHeight)
                .padding(itemPadding)
                .clickable { event.onTargetClick(watcher) }
        )
    }
}

@ThemedPreviews
@Composable
fun WatcherItemPreview() = Preview {
    WatcherItem(
        watcher = Watcher(
            id = 0,
            base = "EUR",
            target = "USD",
            isGreater = false,
            rate = 123456789.0
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
