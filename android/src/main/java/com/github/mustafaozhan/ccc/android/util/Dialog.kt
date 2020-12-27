/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("unused")

package com.github.mustafaozhan.ccc.android.util

import android.app.Activity
import android.app.AlertDialog
import com.github.mustafaozhan.ccc.common.log.kermit
import mustafaozhan.github.com.mycurrencies.R

@Suppress("LongParameterList")
fun showDialog(
    activity: Activity,
    title: String,
    message: String,
    positiveButton: String,
    cancelable: Boolean = true,
    function: (() -> Unit)? = null
) {
    if (!activity.isFinishing) {
        val dialog = AlertDialog
            .Builder(activity, R.style.AlertDialogCustom)
            .setIcon(R.drawable.ic_dialog_and_snackbar)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { _, _ ->
                kermit.d { "Dialog positive button click" }
                function?.invoke()
            }

        if (cancelable) {
            dialog
                .setCancelable(cancelable)
                .setNegativeButton(activity.getString(android.R.string.cancel), null)
        }

        dialog.show()
    }
}

@Suppress("LongParameterList")
fun showDialog(
    activity: Activity,
    title: Int,
    message: Int,
    positiveButton: Int,
    cancelable: Boolean = true,
    function: (() -> Unit)? = null
) {
    if (!activity.isFinishing) {
        val dialog = AlertDialog
            .Builder(activity, R.style.AlertDialogCustom)
            .setIcon(R.drawable.ic_dialog_and_snackbar)
            .setTitle(activity.getString(title))
            .setMessage(activity.getString(message))
            .setPositiveButton(activity.getText(positiveButton)) { _, _ ->
                kermit.d { "Dialog positive button click" }
                function?.invoke()
            }

        if (cancelable) {
            dialog
                .setCancelable(cancelable)
                .setNegativeButton(activity.getString(android.R.string.cancel), null)
        }
        dialog.show()
    }
}

@Suppress("LongParameterList")
fun showSingleChoiceDialog(
    activity: Activity,
    title: String,
    items: Array<String>,
    selectedIndex: Int = 1,
    choiceAction: ((Int) -> Unit)? = null
) {
    if (!activity.isFinishing) {
        AlertDialog
            .Builder(activity, R.style.AlertDialogCustom)
            .setIcon(R.drawable.ic_dialog_and_snackbar)
            .setTitle(title)
            .setSingleChoiceItems(items, selectedIndex) { _, which ->
                kermit.d { "Dialog choice click $which" }
                choiceAction?.invoke(which)
            }.show()
    }
}

@Suppress("LongParameterList")
fun showSingleChoiceDialog(
    activity: Activity,
    title: Int,
    items: Array<String>,
    selectedIndex: Int = 1,
    choiceAction: ((Int) -> Unit)? = null
) {
    if (!activity.isFinishing) {
        AlertDialog
            .Builder(activity, R.style.AlertDialogCustom)
            .setIcon(R.drawable.ic_dialog_and_snackbar)
            .setTitle(activity.getString(title))
            .setSingleChoiceItems(items, selectedIndex) { _, which ->
                kermit.d { "Dialog choice click $which" }
                choiceAction?.invoke(which)
            }.show()
    }
}
