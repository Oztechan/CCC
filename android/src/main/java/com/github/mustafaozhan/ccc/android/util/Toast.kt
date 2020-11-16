/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.util

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import mustafaozhan.github.com.mycurrencies.R

@Suppress("unused")
object Toast {
    private const val imagePadding = 24

    @Suppress("ObjectPropertyName")
    private var _toast: Toast? = null

    fun show(
        context: Context,
        text: String,
        isLong: Boolean = true,
        tintColor: Int? = null
    ) {
        _toast?.cancel()
        _toast = Toast.makeText(
            context,
            text,
            if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        )
        _toast?.view?.apply {
            setBackgroundResource(android.R.drawable.toast_frame)
            background.setTint(
                ContextCompat.getColor(
                    context,
                    tintColor ?: R.color.color_background_toast
                )
            )
            findViewById<TextView>(android.R.id.message)?.apply {
                setTextColor(ContextCompat.getColor(context, R.color.color_text_toast))
                gravity = Gravity.CENTER
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info, 0, 0, 0)
                compoundDrawablePadding = imagePadding
            }
        }

        _toast?.show()
    }

    fun show(
        context: Context,
        text: Int,
        isLong: Boolean = true,
        tintColor: Int? = null
    ) {
        _toast?.cancel()
        _toast = Toast.makeText(
            context,
            context.getString(text),
            if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        )

        _toast?.view?.apply {
            setBackgroundResource(android.R.drawable.toast_frame)
            background.setTint(
                ContextCompat.getColor(
                    context,
                    tintColor ?: R.color.color_background_toast
                )
            )
            findViewById<TextView>(android.R.id.message)?.apply {
                setTextColor(ContextCompat.getColor(context, R.color.color_text_toast))
                gravity = Gravity.CENTER
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info, 0, 0, 0)
                compoundDrawablePadding = imagePadding
            }
        }

        _toast?.show()
    }
}
