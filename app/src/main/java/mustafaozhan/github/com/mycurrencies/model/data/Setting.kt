package mustafaozhan.github.com.mycurrencies.model.data

import ninja.sakib.pultusorm.annotations.PrimaryKey

/**
 * Created by Mustafa Ozhan on 10/9/17 at 1:01 PM on Arch Linux.
 */
data class Setting(@PrimaryKey val name: String? = null, var isActive: String = "true")