package mustafaozhan.github.com.mycurrencies.room.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import mustafaozhan.github.com.mycurrencies.app.Application
import mustafaozhan.github.com.mycurrencies.extensions.getStringByName

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "rate") var rate: Double = 0.0,
    @ColumnInfo(name = "isActive") var isActive: Int = 1,
    @ColumnInfo(name = "description") var description: String =
        Application.instance.getStringByName(name)
)