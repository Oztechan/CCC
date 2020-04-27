// Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
package mustafaozhan.github.com.mycurrencies.data.room.offlineRates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mustafaozhan.github.com.mycurrencies.model.Rates

@Dao
interface OfflineRatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOfflineRates(rates: Rates)

    @Query("SELECT * FROM offline_rates WHERE base=:base")
    fun getOfflineRatesByBase(base: String): Rates?
}
