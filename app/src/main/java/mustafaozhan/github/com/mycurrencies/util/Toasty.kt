package mustafaozhan.github.com.mycurrencies.util

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty
import mustafaozhan.github.com.mycurrencies.R

object Toasty {
    private var toast: Toast? = null

    fun showToasty(
        context: Context,
        text: String,
        isLong: Boolean = true,
        tintColor: Int? = null
    ) {
        toast?.cancel()
        toast = Toasty
            .custom(context,
                text,
                R.drawable.ic_info_outline_black_24dp,
                tintColor ?: R.color.blue_grey_800,
                if (isLong) Toasty.LENGTH_LONG else Toasty.LENGTH_SHORT,
                true,
                true)
        toast?.show()
    }

    fun showToasty(
        context: Context,
        text: Int,
        isLong: Boolean = true,
        tintColor: Int? = null
    ) {
        toast?.cancel()
        toast = Toasty
            .custom(context,
                context.getString(text),
                R.drawable.ic_info_outline_black_24dp,
                tintColor ?: R.color.blue_grey_800,
                if (isLong) Toasty.LENGTH_LONG else Toasty.LENGTH_SHORT,
                true,
                true)
        toast?.show()
    }
}
