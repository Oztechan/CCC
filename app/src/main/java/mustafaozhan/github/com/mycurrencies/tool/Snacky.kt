package mustafaozhan.github.com.mycurrencies.tool

import android.app.Activity
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import de.mateware.snacky.Snacky
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.function.extension.getImageResourceByName
import java.util.Locale

@Suppress("LongParameterList")
fun showSnacky(
    activity: Activity,
    text: String = "",
    actionText: String = "",
    setIcon: String? = null,
    isLong: Boolean = true,
    action: () -> Unit = {}
) = Snacky.builder()
    .setBackgroundColor(ContextCompat.getColor(activity, R.color.blue_grey_800))
    .setText(text)
    .setIcon(setIcon?.let { activity.getImageResourceByName(setIcon) } ?: R.mipmap.ic_launcher)
    .setActivity(activity)
    .setDuration(if (isLong) Snacky.LENGTH_LONG else Snacky.LENGTH_SHORT)
    .setActionText(actionText.toUpperCase(Locale.getDefault()))
    .setActionTextColor(ContextCompat.getColor(activity, R.color.cyan_700))
    .setActionTextTypefaceStyle(Typeface.BOLD)
    .setActionClickListener { action() }
    .build()
    .show()
