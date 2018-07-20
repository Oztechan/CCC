package mustafaozhan.github.com.mycurrencies.room.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
@Entity(tableName = "offline_rates")
data class OfflineRates(
        @PrimaryKey @ColumnInfo(name = "base") var base: String,
        @ColumnInfo(name = "EUR") var eUR: Double? = null,
        @ColumnInfo(name = "AUD") var aUD: Double? = null,
        @ColumnInfo(name = "BGN") var bGN: Double? = null,
        @ColumnInfo(name = "BRL") var bRL: Double? = null,
        @ColumnInfo(name = "CAD") var cAD: Double? = null,
        @ColumnInfo(name = "CHF") var cHF: Double? = null,
        @ColumnInfo(name = "CNY") var cNY: Double? = null,
        @ColumnInfo(name = "CZK") var cZK: Double? = null,
        @ColumnInfo(name = "DKK") var dKK: Double? = null,
        @ColumnInfo(name = "GBP") var gBP: Double? = null,
        @ColumnInfo(name = "HKD") var hKD: Double? = null,
        @ColumnInfo(name = "HRK") var hRK: Double? = null,
        @ColumnInfo(name = "HUF") var hUF: Double? = null,
        @ColumnInfo(name = "IDR") var iDR: Double? = null,
        @ColumnInfo(name = "ILS") var iLS: Double? = null,
        @ColumnInfo(name = "INR") var iNR: Double? = null,
        @ColumnInfo(name = "JPY") var jPY: Double? = null,
        @ColumnInfo(name = "KRW") var kRW: Double? = null,
        @ColumnInfo(name = "MXN") var mXN: Double? = null,
        @ColumnInfo(name = "MYR") var mYR: Double? = null,
        @ColumnInfo(name = "NOK") var nOK: Double? = null,
        @ColumnInfo(name = "NZD") var nZD: Double? = null,
        @ColumnInfo(name = "PHP") var pHP: Double? = null,
        @ColumnInfo(name = "PLN") var pLN: Double? = null,
        @ColumnInfo(name = "RON") var rON: Double? = null,
        @ColumnInfo(name = "RUB") var rUB: Double? = null,
        @ColumnInfo(name = "SEK") var sEK: Double? = null,
        @ColumnInfo(name = "SGD") var sGD: Double? = null,
        @ColumnInfo(name = "THB") var tHB: Double? = null,
        @ColumnInfo(name = "TRY") var tRY: Double? = null,
        @ColumnInfo(name = "USD") var uSD: Double? = null,
        @ColumnInfo(name = "ZAR") var zAR: Double? = null
)