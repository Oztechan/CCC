package com.oztechan.ccc.android.ui.widget.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.oztechan.ccc.android.ui.widget.R
import com.oztechan.ccc.android.viewmodel.widget.WidgetEvent
import com.oztechan.ccc.android.viewmodel.widget.WidgetState

@Suppress("RestrictedApi")
@Composable
fun WidgetView(
    state: WidgetState,
    event: WidgetEvent
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(R.color.background),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        if (state.isPremium) {
            HeaderView(
                currentBase = state.currentBase,
                onBackClick = event::onPreviousClick,
                onNextClick = event::onNextClick
            )

            state.currencyList.forEach {
                WidgetItem(item = it)
            }
        } else {
            Spacer(modifier = GlanceModifier.defaultWeight())

            Text(
                text = LocalContext.current.getString(R.string.txt_widget_available_only_premium),
                modifier = GlanceModifier.padding(8.dp),
                style = TextStyle(
                    color = ColorProvider(R.color.text),
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = GlanceModifier.defaultWeight())

        FooterView(lastUpdate = state.lastUpdate)
    }
}
