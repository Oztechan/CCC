package com.oztechan.ccc.client.storage.app

import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.core.persistence.SuspendPersistence

internal class AppStorageImpl(
    private val persistence: Persistence,
    private val suspendPersistence: SuspendPersistence
) : AppStorage {
    override suspend fun isFirstRun(): Boolean =
        suspendPersistence.getSuspend(KEY_FIRST_RUN, DEFAULT_FIRST_RUN)

    override suspend fun setFirstRun(value: Boolean) =
        suspendPersistence.setSuspend(KEY_FIRST_RUN, value)

    override suspend fun getAppTheme(): Int =
        suspendPersistence.getSuspend(KEY_APP_THEME, DEFAULT_APP_THEME)

    override suspend fun setAppTheme(value: Int) =
        suspendPersistence.setSuspend(KEY_APP_THEME, value)

    override suspend fun getPremiumEndDate(): Long =
        suspendPersistence.getSuspend(KEY_PREMIUM_END_DATE, DEFAULT_PREMIUM_END_DATE)

    override suspend fun setPremiumEndDate(value: Long) =
        suspendPersistence.setSuspend(KEY_PREMIUM_END_DATE, value)

    override var sessionCount: Long
        get() = persistence.getValue(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT)
        set(value) = persistence.setValue(KEY_SESSION_COUNT, value)

    companion object {
        internal const val KEY_FIRST_RUN = "firs_run"
        internal const val KEY_PREMIUM_END_DATE = "ad_free_end_date"
        internal const val KEY_APP_THEME = "app_theme"
        internal const val KEY_SESSION_COUNT = "session_count"

        internal const val DEFAULT_FIRST_RUN = true
        internal const val DEFAULT_PREMIUM_END_DATE = 0L
        internal const val DEFAULT_APP_THEME = -1
        internal const val DEFAULT_SESSION_COUNT = 0L
    }
}
