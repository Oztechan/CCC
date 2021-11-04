/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("unused")

package com.github.mustafaozhan.ccc.android.util

import android.app.Activity
import android.app.AlertDialog
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.scopemob.inCase
import com.github.mustafaozhan.scopemob.whetherNot
import mustafaozhan.github.com.mycurrencies.R

@Suppress("LongParameterList")
fun showDialog(
    activity: Activity,
    title: String,
    message: String,
    positiveButton: String,
    cancelable: Boolean = true,
    function: (() -> Unit)? = null
) = buildDialog(activity, title)
    ?.setMessage(message)
    ?.setPositiveButton(positiveButton) { dialog, _ ->
        Logger.i { "Dialog positive button click" }
        function?.invoke()
        dialog.dismiss()
    }
    ?.inCase(cancelable) {
        setCancelable(cancelable)
        setNegativeButton(activity.getString(android.R.string.cancel), null)
    }?.show()

@Suppress("LongParameterList")
fun showDialog(
    activity: Activity,
    title: Int,
    message: Int,
    positiveButton: Int,
    cancelable: Boolean = true,
    function: (() -> Unit)? = null
) = showDialog(
    activity,
    activity.getString(title),
    activity.getString(message),
    activity.getString(positiveButton),
    cancelable,
    function
)

@Suppress("LongParameterList")
fun showSingleChoiceDialog(
    activity: Activity,
    title: String,
    items: Array<String>,
    selectedIndex: Int = 1,
    choiceAction: ((Int) -> Unit)? = null
) = buildDialog(activity, title)
    ?.setSingleChoiceItems(items, selectedIndex) { dialog, which ->
        Logger.i { "Dialog choice click $which" }
        choiceAction?.invoke(which)
        dialog.dismiss()
    }?.show()

@Suppress("LongParameterList")
fun showSingleChoiceDialog(
    activity: Activity,
    title: Int,
    items: Array<String>,
    selectedIndex: Int = 1,
    choiceAction: ((Int) -> Unit)? = null
) = showSingleChoiceDialog(
    activity,
    activity.getString(title),
    items,
    selectedIndex,
    choiceAction
)

private fun buildDialog(
    activity: Activity,
    title: String
) = AlertDialog
    .Builder(activity, R.style.AlertDialogCustom)
    .whetherNot { activity.isFinishing }
    ?.setIcon(R.drawable.ic_dialog_and_snackbar)
    ?.setTitle(title)
