/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.android.ui.mobile.util

import android.app.Activity
import android.app.AlertDialog
import co.touchlab.kermit.Logger
import com.github.submob.scopemob.inCase
import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.android.ui.mobile.R

fun Activity.showDialog(
    title: String,
    message: String,
    positiveButton: String,
    cancelable: Boolean = true,
    function: (() -> Unit)? = null
) = buildDialog(title)
    ?.setMessage(message)
    ?.setPositiveButton(positiveButton) { dialog, _ ->
        Logger.i { "Dialog positive button click" }
        function?.invoke()
        dialog.dismiss()
    }
    ?.setCancelable(cancelable)
    ?.inCase(cancelable) {
        it.setNegativeButton(getString(android.R.string.cancel), null)
    }?.show()

fun Activity.showDialog(
    title: Int,
    message: Int,
    positiveButton: Int,
    cancelable: Boolean = true,
    function: (() -> Unit)? = null
) = showDialog(
    title = getString(title),
    message = getString(message),
    positiveButton = getString(positiveButton),
    cancelable = cancelable,
    function = function
)

fun Activity.showSingleChoiceDialog(
    title: String,
    items: Array<String>,
    selectedIndex: Int = 1,
    choiceAction: ((Int) -> Unit)? = null
) = buildDialog(title)
    ?.setSingleChoiceItems(items, selectedIndex) { dialog, which ->
        Logger.i { "Dialog choice click $which" }
        choiceAction?.invoke(which)
        dialog.dismiss()
    }?.show()

fun Activity.showSingleChoiceDialog(
    title: Int,
    items: Array<String>,
    selectedIndex: Int = 1,
    choiceAction: ((Int) -> Unit)? = null
) = showSingleChoiceDialog(
    title = getString(title),
    items = items,
    selectedIndex = selectedIndex,
    choiceAction = choiceAction
)

private fun Activity.buildDialog(title: String) = AlertDialog
    .Builder(this, R.style.AlertDialogCustom)
    .whetherNot { isFinishing }
    ?.setIcon(R.drawable.ic_app_logo)
    ?.setTitle(title)
