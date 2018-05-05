package mustafaozhan.github.com.mycurrencies.model.web

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Mustafa Ozhan on 9/6/17 at 7:57 PM on Arch Linux.
 */
class ResponseAll {

    @SerializedName("base")
    @Expose
    var base: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("rates")
    @Expose
    var rates: Rates? = null

}