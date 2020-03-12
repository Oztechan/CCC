package mustafaozhan.github.com.mycurrencies.extension

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.jaredrummler.materialspinner.MaterialSpinner
import mustafaozhan.github.com.mycurrencies.R
import timber.log.Timber
import java.io.FileNotFoundException
import java.util.Locale

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */

fun ImageView.setBackgroundByName(name: String) =
    setImageResource(context.getImageResourceByName(name))

fun Context.getImageResourceByName(name: String): Int =
    try {
        resources.getIdentifier(
            name.toLowerCase(Locale.getDefault()).replace("try", "tryy"),
            "drawable",
            packageName
        )
    } catch (e: FileNotFoundException) {
        Timber.w(e)
        R.drawable.transparent
    }

@SuppressLint("SetTextI18n")
fun TextView.addText(str: String) {
    text = text.toString() + str
}

fun AdView.checkAd(id: Int, isExpired: Boolean) =
    if (isExpired) {
        MobileAds.initialize(context, resources.getString(id))
        val adRequest = AdRequest.Builder().build()
        loadAd(adRequest)
    } else {
        isEnabled = false
        visibility = View.GONE
    }

fun MaterialSpinner.tryToSelect(indexOf: Int) {
    selectedIndex = try {
        indexOf
    } catch (exception: IllegalArgumentException) {
        Timber.w(exception, "try to select failed for index $indexOf")
        0
    }
}

fun View?.visible() {
    this?.bringToFront()
    this?.visibility = View.VISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}
