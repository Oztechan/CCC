package mustafaozhan.github.com.mycurrencies.tool

import android.graphics.Typeface
import android.view.View
import androidx.core.content.ContextCompat
import de.mateware.snacky.Snacky
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.function.extension.getImageResourceByName

@Suppress("LongParameterList")
fun showSnacky(
    view: View?,
    text: Int? = null,
    actionText: Int? = null,
    setIcon: String? = null,
    isLong: Boolean = true,
    action: () -> Unit = {}
) = view?.apply {
    Snacky.builder()
        .setBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey_800))
        .setText(text?.let { context.getString(it) } ?: "")
        .setIcon(setIcon?.let { context.getImageResourceByName(setIcon) } ?: R.mipmap.ic_launcher)
        .setView(this)
        .setDuration(if (isLong) Snacky.LENGTH_LONG else Snacky.LENGTH_SHORT)
        .setActionText(actionText?.let { context.getString(it) } ?: "")
        .setActionTextColor(ContextCompat.getColor(context, R.color.cyan_700))
        .setActionTextTypefaceStyle(Typeface.BOLD)
        .setActionClickListener { action() }
        .build()
        .show()
}

@Suppress("LongParameterList")
fun showSnacky(
    view: View?,
    text: String = "",
    actionText: String = "",
    setIcon: String? = null,
    isLong: Boolean = true,
    action: () -> Unit = {}
) = view?.apply {
    Snacky.builder()
        .setBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey_800))
        .setText(text)
        .setIcon(setIcon?.let { context.getImageResourceByName(setIcon) } ?: R.mipmap.ic_launcher)
        .setView(this)
        .setDuration(if (isLong) Snacky.LENGTH_LONG else Snacky.LENGTH_SHORT)
        .setActionText(actionText)
        .setActionTextColor(ContextCompat.getColor(context, R.color.cyan_700))
        .setActionTextTypefaceStyle(Typeface.BOLD)
        .setActionClickListener { action() }
        .build()
        .show()
}
