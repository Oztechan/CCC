package com.oztechan.ccc.android.widget.ui

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import com.oztechan.ccc.android.R
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
        HeaderView(currentBase = state.currentBase)

        state.currencyList.forEach {
            WidgetItem(item = it)
        }

        Spacer(modifier = GlanceModifier.defaultWeight())

        FooterView(lastUpdate = state.lastUpdate)
    }
}
