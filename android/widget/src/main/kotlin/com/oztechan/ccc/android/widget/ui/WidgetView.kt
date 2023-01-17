package com.oztechan.ccc.android.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.oztechan.ccc.android.widget.R
import com.oztechan.ccc.client.viewmodel.widget.WidgetState

@Composable
fun WidgetView(state: WidgetState) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(R.color.background),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        if (state.isPremium) {
            HeaderView(currentBase = state.currentBase)

            state.currencyList.forEach {
                WidgetItem(item = it)
            }
        } else {
            Spacer(modifier = GlanceModifier.defaultWeight())

            Text(
                text = "Widget is only available in Premium",
                modifier = GlanceModifier.padding(8.dp),
                style = TextStyle(textAlign = TextAlign.Center)
            )
        }

        Spacer(modifier = GlanceModifier.defaultWeight())

        FooterView(lastUpdate = state.lastUpdate)
    }
}
