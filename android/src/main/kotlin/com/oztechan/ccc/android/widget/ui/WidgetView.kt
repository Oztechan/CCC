package com.oztechan.ccc.android.widget.ui

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import com.oztechan.ccc.android.R
import com.oztechan.ccc.client.viewmodel.widget.WidgetViewModel

@Suppress("FunctionNaming")
@Composable
fun WidgetView(viewModel: WidgetViewModel) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(R.color.background),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        HeaderView(currentBase = viewModel.currentBase)

        viewModel.currencyList.forEach {
            WidgetItem(item = it)
        }

        Spacer(modifier = GlanceModifier.defaultWeight())

        FooterView(lastUpdate = viewModel.lastUpdate)
    }
}
