package com.oztechan.ccc.client.storage.app

import com.oztechan.ccc.client.core.persistence.Persistence
import com.oztechan.ccc.client.core.persistence.SuspendPersistence
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_APP_THEME
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_FIRST_RUN
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_PREMIUM_END_DATE
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.DEFAULT_SESSION_COUNT
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_APP_THEME
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_FIRST_RUN
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_PREMIUM_END_DATE
import com.oztechan.ccc.client.storage.app.AppStorageImpl.Companion.KEY_SESSION_COUNT
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.configure
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AppStorageTest {

    private val subject: AppStorage by lazy {
        AppStorageImpl(persistence, suspendPersistence)
    }

    @Mock
    private val persistence = configure(mock(classOf<Persistence>())) { stubsUnitByDefault = true }

    @Mock
    private val suspendPersistence = mock(classOf<SuspendPersistence>())

    // defaults
    @Test
    fun `get default firstRun`() = runTest {
        coEvery { suspendPersistence.getSuspend(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
            .returns(DEFAULT_FIRST_RUN)

        assertEquals(DEFAULT_FIRST_RUN, subject.isFirstRun())

        coVerify { suspendPersistence.getSuspend(KEY_FIRST_RUN, DEFAULT_FIRST_RUN) }
            .wasInvoked()
    }

    @Test
    fun `get default appTheme`() = runTest {
        coEvery { suspendPersistence.getSuspend(KEY_APP_THEME, DEFAULT_APP_THEME) }
            .returns(DEFAULT_APP_THEME)

        assertEquals(DEFAULT_APP_THEME, subject.getAppTheme())

        coVerify { suspendPersistence.getSuspend(KEY_APP_THEME, DEFAULT_APP_THEME) }
            .wasInvoked()
    }

    @Test
    fun `get default premiumEndDate`() = runTest {
        coEvery { suspendPersistence.getSuspend(KEY_PREMIUM_END_DATE, DEFAULT_PREMIUM_END_DATE) }
            .returns(DEFAULT_PREMIUM_END_DATE)

        assertEquals(DEFAULT_PREMIUM_END_DATE, subject.getPremiumEndDate())

        coVerify { suspendPersistence.getSuspend(KEY_PREMIUM_END_DATE, DEFAULT_PREMIUM_END_DATE) }
            .wasInvoked()
    }

    @Test
    fun `default sessionCount`() {
        every { persistence.getValue(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
            .returns(DEFAULT_SESSION_COUNT)

        assertEquals(DEFAULT_SESSION_COUNT, subject.sessionCount)

        verify { persistence.getValue(KEY_SESSION_COUNT, DEFAULT_SESSION_COUNT) }
            .wasInvoked()
    }

    // setters
    @Test
    fun `set firstRun`() = runTest {
        val mockedValue = Random.nextBoolean()
        subject.setFirstRun(mockedValue)

        coVerify { suspendPersistence.setSuspend(KEY_FIRST_RUN, mockedValue) }
            .wasInvoked()
    }

    @Test
    fun `set appTheme`() = runTest {
        val mockValue = Random.nextInt()
        subject.setAppTheme(mockValue)

        coVerify { suspendPersistence.setSuspend(KEY_APP_THEME, mockValue) }
            .wasInvoked()
    }

    @Test
    fun `set premiumEndDate`() = runTest {
        val mockValue = Random.nextLong()
        subject.setPremiumEndDate(mockValue)

        coVerify { suspendPersistence.setSuspend(KEY_PREMIUM_END_DATE, mockValue) }
            .wasInvoked()
    }

    @Test
    fun `set sessionCount`() {
        val mockValue = Random.nextLong()
        subject.sessionCount = mockValue

        verify { persistence.setValue(KEY_SESSION_COUNT, mockValue) }
            .wasInvoked()
    }
}
