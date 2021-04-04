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
    private const val IMAGE_PADDING = 24

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
            context.applicationContext,
            text,
            if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        )

        @Suppress("DEPRECATION")
        _toast?.view?.apply {
            setBackgroundResource(android.R.drawable.toast_frame)
            background.setTint(
                ContextCompat.getColor(
                    context.applicationContext,
                    tintColor ?: R.color.color_background_toast
                )
            )
            findViewById<TextView>(android.R.id.message)?.apply {
                setTextColor(
                    ContextCompat.getColor(
                        context.applicationContext,
                        R.color.color_text_toast
                    )
                )
                gravity = Gravity.CENTER
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info, 0, 0, 0)
                compoundDrawablePadding = IMAGE_PADDING
            }
        }

        _toast?.show()
    }

    fun show(
        context: Context,
        text: Int,
        isLong: Boolean = true,
        tintColor: Int? = null
    ) = show(context, context.getString(text), isLong, tintColor)
}
