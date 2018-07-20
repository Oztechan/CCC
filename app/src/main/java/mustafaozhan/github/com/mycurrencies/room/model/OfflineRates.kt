package mustafaozhan.github.com.mycurrencies.room.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
@Entity(tableName = "offline_rates")
data class OfflineRates(
        @PrimaryKey @ColumnInfo(name = "base") var base: String,
        @ColumnInfo(name = "rates") var rates: Rates)