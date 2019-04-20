package mustafaozhan.github.com.mycurrencies.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivity
import java.io.FileNotFoundException

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */

const val MAX_DIGIT = 18

fun ImageView.setBackgroundByName(name: String) =
    setImageResource(context.getImageResourceByName(name))

fun Context.getImageResourceByName(name: String): Int =
    try {
        resources.getIdentifier(
            name.toLowerCase().replace("try", "tryy"),
            "drawable",
            packageName
        )
    } catch (e: FileNotFoundException) {
        Crashlytics.logException(e)
        R.drawable.transparent
    }

@SuppressLint("SetTextI18n")
fun TextView.addText(str: String) =
    if (text.toString().length < MAX_DIGIT)
        text = text.toString() + str
    else
        (context as MainActivity).snacky(context.getString(R.string.max_input), isLong = false)

fun AdView.loadAd(adId: Int) {
    MobileAds.initialize(context, resources.getString(adId))
    val adRequest = AdRequest.Builder().build()
    loadAd(adRequest)
}