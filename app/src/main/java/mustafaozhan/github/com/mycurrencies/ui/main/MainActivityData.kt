package mustafaozhan.github.com.mycurrencies.ui.main

import com.github.mustafaozhan.basemob.model.BaseData

open class MainActivityData : BaseData() {
    companion object {
        const val MINIMUM_ACTIVE_CURRENCY = 2
        const val BACK_DELAY: Long = 2
        const val AD_INITIAL_DELAY: Long = 50
        const val AD_PERIOD: Long = 250
        const val TEXT_EMAIL_TYPE = "text/email"
    }
}
