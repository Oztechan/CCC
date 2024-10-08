/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.util

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import co.touchlab.kermit.Logger
import com.google.android.material.snackbar.Snackbar
import com.oztechan.ccc.android.ui.mobile.R

@Suppress("NestedBlockDepth")
fun View?.showSnack(
    text: String = "",
    actionText: String = "",
    icon: Int? = null,
    isIndefinite: Boolean = false,
    action: (() -> Unit)? = null
) = this?.let {
    Snackbar.make(it, text, if (isIndefinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG).apply {
        setAction(actionText) {
            Logger.i { "Snackbar action click" }
            action?.invoke()
        }
        view.apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.color_background_snack_bar))

            findViewById<TextView>(R.id.snackbar_text)?.apply {
                gravity = Gravity.CENTER

                ContextCompat.getDrawable(context, icon ?: R.drawable.ic_app_logo)?.apply {
                    setBounds(
                        0,
                        0,
                        resources.getDimensionPixelSize(R.dimen.snack_icon_size),
                        resources.getDimensionPixelSize(R.dimen.snack_icon_size)
                    )
                }?.let { drawable ->
                    setCompoundDrawables(drawable, null, null, null)
                    compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.margin_eight)
                }
            }

            findViewById<TextView>(R.id.snackbar_action)?.apply {
                setTypeface(null, Typeface.BOLD)
                setTextColor(ContextCompat.getColor(context, R.color.color_text_action_snack_bar))
            }
        }
    }.show()
}

fun View?.showSnack(
    text: Int? = null,
    actionText: Int? = null,
    icon: Int? = null,
    isIndefinite: Boolean = false,
    action: (() -> Unit)? = null
) = this?.showSnack(
    text = if (text == null) "" else context.getString(text),
    actionText = if (actionText == null) "" else context.getString(actionText),
    icon = icon,
    isIndefinite = isIndefinite,
    action = action
)
