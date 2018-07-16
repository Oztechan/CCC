package mustafaozhan.github.com.mycurrencies.room.dao

import android.arch.persistence.room.*
import mustafaozhan.github.com.mycurrencies.room.model.Setting

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Dao
abstract class SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSetting(setting: Setting)

    @Update
    abstract fun updateSetting(setting: Setting)

    @Query("SELECT * FROM setting")
    abstract fun getAllSettings(): MutableList<Setting>

    @Query("SELECT * FROM setting WHERE isActive==1")
    abstract fun getActiveSettings(): MutableList<Setting>

}