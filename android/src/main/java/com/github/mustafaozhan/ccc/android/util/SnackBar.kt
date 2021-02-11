/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.util

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mustafaozhan.ccc.client.log.kermit
import com.google.android.material.snackbar.Snackbar
import mustafaozhan.github.com.mycurrencies.R

private const val IMAGE_PADDING = 24

@Suppress("LongParameterList")
fun showSnack(
    view: View,
    text: String = "",
    actionText: String = "",
    icon: Int? = null,
    isIndefinite: Boolean = false,
    action: (() -> Unit)? = null
) = Snackbar.make(
    view,
    text,
    if (isIndefinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
).apply {
    setAction(actionText) {
        kermit.d { "Snackbar action click" }
        action?.invoke()
    }
    this.view.apply {
        setBackgroundColor(ContextCompat.getColor(context, R.color.color_background_snack_bar))
        findViewById<TextView>(R.id.snackbar_text)?.apply {
            gravity = Gravity.CENTER
            setCompoundDrawablesWithIntrinsicBounds(
                icon ?: R.drawable.ic_dialog_and_snackbar,
                0,
                0,
                0
            )
            compoundDrawablePadding = IMAGE_PADDING
        }
        findViewById<TextView>(R.id.snackbar_action)?.apply {
            setTypeface(null, Typeface.BOLD)
            setTextColor(ContextCompat.getColor(context, R.color.color_text_action_snack_bar))
        }
    }
}.show()

@Suppress("LongParameterList")
fun showSnack(
    view: View,
    text: Int? = null,
    actionText: Int? = null,
    icon: Int? = null,
    isIndefinite: Boolean = false,
    action: (() -> Unit)? = null
) = showSnack(
    view,
    if (text == null) "" else view.context.getString(text),
    if (actionText == null) "" else view.context.getString(actionText),
    icon,
    isIndefinite,
    action
)
