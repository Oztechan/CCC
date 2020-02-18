package mustafaozhan.github.com.mycurrencies.util

import android.app.AlertDialog
import android.content.Context
import mustafaozhan.github.com.mycurrencies.R

@Suppress("LongParameterList")
fun showDialog(
    context: Context,
    title: String,
    description: String,
    positiveButton: String,
    cancelable: Boolean = true,
    function: () -> Unit = {}
) {
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
}
