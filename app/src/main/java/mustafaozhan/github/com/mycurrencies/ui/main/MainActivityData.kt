/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main

import com.github.mustafaozhan.basemob.model.BaseData

open class MainActivityData : BaseData() {
    companion object {
        internal const val MINIMUM_ACTIVE_CURRENCY = 2
        internal const val BACK_DELAY: Long = 2000
        internal const val AD_INITIAL_DELAY: Long = 45000
        internal const val AD_PERIOD: Long = 180000
        internal const val TEXT_EMAIL_TYPE = "text/email"
    }
}
