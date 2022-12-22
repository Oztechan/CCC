@file:OptIn(ExperimentalMaterial3Api::class)

package com.oztechan.ccc.android.ui.compose.content.watchers

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oztechan.ccc.android.R
import com.oztechan.ccc.android.ui.compose.annotations.ProjectPreviews
import com.oztechan.ccc.android.ui.compose.component.ImageView
import com.oztechan.ccc.android.ui.compose.theme.AppTheme
import com.oztechan.ccc.client.model.Watcher
import com.oztechan.ccc.res.getImageResourceIdByName

@Suppress("FunctionNaming", "LongMethod")
@Composable
fun WatcherItem(
    watcher: Watcher,
    onTextChange: (String) -> Unit
) {
    val itemPadding: Dp = 2.dp
    val itemHeight: Dp = 36.dp

    Row(
        modifier = Modifier
            .background(color = colorResource(id = R.color.background))
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.one),
            modifier = Modifier.padding(itemPadding),
            color = colorResource(id = R.color.text)
        )

        Text(
            text = watcher.base,
            modifier = Modifier.padding(itemPadding),
            color = colorResource(id = R.color.text)
        )

        ImageView(
            painter = painterResource(getImageResourceIdByName(watcher.base)),
            modifier = Modifier
                .size(itemHeight)
                .padding(itemPadding)
        )

        Text(
            text = "is greater than",
            modifier = Modifier.padding(itemPadding),
            color = colorResource(id = R.color.text)
        )

        Spacer(Modifier.weight(1f))

        OutlinedTextField(
            value = watcher.rate,
            onValueChange = onTextChange,
            modifier = Modifier
                .padding(itemPadding)
                .width(105.dp),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.txt_rate),
                    color = colorResource(id = R.color.text)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = TextStyle(textAlign = TextAlign.Center)
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = watcher.target,
            modifier = Modifier.padding(itemPadding),
            color = colorResource(id = R.color.text)
        )

        ImageView(
            painter = painterResource(getImageResourceIdByName(watcher.target)),
            modifier = Modifier
                .size(itemHeight)
                .padding(itemPadding)
        )
    }
}

@ProjectPreviews
@Composable
@Suppress("FunctionNaming")
fun WatcherItemPreview() {
    AppTheme {
        WatcherItem(
            Watcher(
                id = 0,
                base = "EUR",
                target = "USD",
                isGreater = false,
                rate = "123456789"
            )
        ) {}
    }
}
