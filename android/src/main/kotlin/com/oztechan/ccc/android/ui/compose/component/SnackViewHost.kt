package com.oztechan.ccc.android.ui.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oztechan.ccc.android.R
import com.oztechan.ccc.android.ui.compose.annotations.ThemedPreviews
import com.oztechan.ccc.android.ui.compose.util.toColor
import com.oztechan.ccc.android.ui.compose.util.toPainter

@Composable
fun SnackViewHost(
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit
) {
    Box {
        content()

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            SnackViewContent(it)
        }
    }
}

@Composable
fun SnackViewContent(snackbarData: SnackbarData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = R.color.background_weak.toColor(),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp)
    ) {
        ImageView(
            painter = R.drawable.ic_app_logo.toPainter(),
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
        )

        Text(
            text = snackbarData.visuals.message,
            modifier = Modifier.padding(2.dp),
            fontSize = 14.sp
        )

        Spacer(Modifier.weight(1f))

        TextButton(
            onClick = { snackbarData.performAction() },
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = snackbarData.visuals.actionLabel.orEmpty(),
                fontSize = 14.sp
            )
        }
    }
}

@Suppress("UnrememberedMutableState")
@Composable
@ThemedPreviews
fun SnackViewContentPreview() = Preview {
    SnackViewContent(
        snackbarData = object : SnackbarData {
            override val visuals = object : SnackbarVisuals {
                override val actionLabel: String = "Label"
                override val duration: SnackbarDuration = SnackbarDuration.Indefinite
                override val message: String = "Message"
                override val withDismissAction: Boolean = false
            }

            override fun dismiss() = Unit
            override fun performAction() = Unit
        }
    )
}
