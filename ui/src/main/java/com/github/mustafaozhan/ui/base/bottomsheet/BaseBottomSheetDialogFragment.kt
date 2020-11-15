/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ui.base.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.mustafaozhan.ui.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.android.support.AndroidSupportInjection

abstract class BaseBottomSheetDialogFragment : AppCompatDialogFragment() {

    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    protected fun dismissDialog() = findNavController().navigateUp()

    fun navigate(
        currentDestinationId: Int,
        navDirections: NavDirections,
        dismiss: Boolean,
        animate: Boolean = true
    ) = findNavController().apply {
        if (currentDestination?.id == currentDestinationId) {
            val navigationBuilder = NavOptions.Builder().setLaunchSingleTop(true)

            if (animate) {
                navigationBuilder.setEnterAnim(R.anim.enter_from_right)
                    .setExitAnim(R.anim.exit_to_left)
                    .setPopEnterAnim(R.anim.enter_from_left)
                    .setPopExitAnim(R.anim.exit_to_right)
            }

            navigate(navDirections, navigationBuilder.build())

            if (dismiss) navigateUp()
        }
    }
}
