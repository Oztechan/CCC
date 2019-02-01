package mustafaozhan.github.com.mycurrencies.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import mustafaozhan.github.com.mycurrencies.room.model.OfflineRates

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
@Dao
abstract class OfflineRatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOfflineRates(offlineRates: OfflineRates)

    @Query("SELECT * FROM offline_rates WHERE base=:base")
    abstract fun getOfflineRatesOnBase(base: String): OfflineRates?

}