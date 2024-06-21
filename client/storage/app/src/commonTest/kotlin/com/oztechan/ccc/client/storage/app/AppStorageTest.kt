package com.oztechan.ccc.client.storage.app

import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_APP_THEME
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_FIRST_RUN
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_PREMIUM_END_DATE
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_SESSION_COUNT
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_APP_THEME
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_FIRST_RUN
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_PREMIUM_END_DATE
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_SESSION_COUNT
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AppStorageTest {

    private val subject: AppStorage by lazy {
        AppStorageImpl(persistence)
    }

    private val persistence = mock<Persistence>(MockMode.autoUnit)

    // defaults
    @Test
    fun `default firstRun`() {
        every { persistence.getValue(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
            .returns(DEFAULT_FIRST_RUN)

        assertEquals(DEFAULT_FIRST_RUN, subject.firstRun)

        verify { persistence.getValue(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
    }

    @Test
    fun `default appTheme`() {
        every { persistence.getValue(KEY_APP_THEME, DEFAULT_APP_THEME) }
            .returns(DEFAULT_APP_THEME)

        assertEquals(DEFAULT_APP_THEME, subject.appTheme)

        verify { persistence.getValue(KEY_APP_THEME, DEFAULT_APP_THEME) }
    }

    @Test
    fun `default premiumEndDate`() {
        every { persistence.getValue(KEY_PREMIUM_END_DATE, DEFAULT_PREMIUM_END_DATE) }
            .returns(DEFAULT_PREMIUM_END_DATE)

        assertEquals(DEFAULT_PREMIUM_END_DATE, subject.premiumEndDate)

        verify { persistence.getValue(KEY_PREMIUM_END_DATE, DEFAULT_PREMIUM_END_DATE) }
    }

    @Test
    fun `default sessionCount`() {
        every { persistence.getValue(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
            .returns(DEFAULT_SESSION_COUNT)

        assertEquals(DEFAULT_SESSION_COUNT, subject.sessionCount)

        verify { persistence.getValue(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
    }

    // setters
    @Test
    fun `set firstRun`() {
        val mockedValue = Random.nextBoolean()
        subject.firstRun = mockedValue

        verify { persistence.setValue(KEY_FIRST_RUN, mockedValue) }
    }

    @Test
    fun `set appTheme`() {
        val mockValue = Random.nextInt()
        subject.appTheme = mockValue

        verify { persistence.setValue(KEY_APP_THEME, mockValue) }
    }

    @Test
    fun `set premiumEndDate`() {
        val mockValue = Random.nextLong()
        subject.premiumEndDate = mockValue

        verify { persistence.setValue(KEY_PREMIUM_END_DATE, mockValue) }
    }

    @Test
    fun `set sessionCount`() {
        val mockValue = Random.nextLong()
        subject.sessionCount = mockValue

        verify { persistence.setValue(KEY_SESSION_COUNT, mockValue) }
    }
}
