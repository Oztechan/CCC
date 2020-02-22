package mustafaozhan.github.com.mycurrencies.tool

import android.app.AlertDialog
import android.content.Context
import android.view.WindowManager
import mustafaozhan.github.com.mycurrencies.R
import timber.log.Timber

@Suppress("LongParameterList")
fun showDialog(
    context: Context,
    title: String,
    description: String,
    positiveButton: String,
    cancelable: Boolean = true,
    function: () -> Unit = {}
) {
    try {
        val builder = AlertDialog
            .Builder(context, R.style.AlertDialogCustom)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(positiveButton) { _, _ -> function() }
            .setCancelable(cancelable)

        if (cancelable) {
            builder.setNegativeButton(context.getString(R.string.cancel), null)
        }

        builder.show()
    } catch (e: WindowManager.BadTokenException) {
        Timber.w(e, "Activity is not running")
    }
}

@Suppress("LongParameterList")
fun showDialog(
    context: Context,
    title: Int,
    description: Int,
    positiveButton: Int,
    cancelable: Boolean = true,
    function: () -> Unit = {}
) {
    try {
        val builder = AlertDialog
            .Builder(context, R.style.AlertDialogCustom)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle(context.getString(title))
            .setMessage(context.getString(description))
            .setPositiveButton(context.getText(positiveButton)) { _, _ -> function() }
            .setCancelable(cancelable)

        if (cancelable) {
            builder.setNegativeButton(context.getString(R.string.cancel), null)
        }

        builder.show()
    } catch (e: WindowManager.BadTokenException) {
        Timber.w(e, "Activity is not running")
    }
}
