package mustafaozhan.github.com.mycurrencies.model.data

import ninja.sakib.pultusorm.annotations.PrimaryKey

/**
 * Created by Mustafa Ozhan on 10/7/17 at 7:09 PM on Arch Linux.
 */
data class Currency(@PrimaryKey val name:String, val rate:Double)