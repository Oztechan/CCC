package com.oztechan.ccc.client.core.persistence

import com.oztechan.ccc.client.core.persistence.fakes.Fakes.KEY
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockBoolean
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockFloat
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockInt
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockLong
import com.oztechan.ccc.client.core.persistence.fakes.Fakes.mockString
import com.russhwolf.settings.coroutines.FlowSettings
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.configure
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("OPT_IN_USAGE")
internal class FlowPersistenceTest {
    private val flowPersistence: FlowPersistence by lazy {
        FlowPersistenceImpl(flowSettings)
    }

    @Mock
    private val flowSettings = configure(mock(classOf<FlowSettings>())) {
        stubsUnitByDefault = true
    }

    @Test
    fun `getFlow returns the same type`() = runTest {
        every { flowSettings.getFloatFlow(KEY, mockFloat) }
            .returns(flowOf(mockFloat))
        every { flowSettings.getBooleanFlow(KEY, mockBoolean) }
            .returns(flowOf(mockBoolean))
        every { flowSettings.getIntFlow(KEY, mockInt) }
            .returns(flowOf(mockInt))
        every { flowSettings.getStringFlow(KEY, mockString) }
            .returns(flowOf(mockString))
        every { flowSettings.getLongFlow(KEY, mockLong) }
            .returns(flowOf(mockLong))

        assertEquals(mockFloat, flowPersistence.getFlow(KEY, mockFloat).firstOrNull())
        assertEquals(mockBoolean, flowPersistence.getFlow(KEY, mockBoolean).firstOrNull())
        assertEquals(mockInt, flowPersistence.getFlow(KEY, mockInt).firstOrNull())
        assertEquals(mockString, flowPersistence.getFlow(KEY, mockString).firstOrNull())
        assertEquals(mockLong, flowPersistence.getFlow(KEY, mockLong).firstOrNull())

        verify { flowSettings.getFloatFlow(KEY, mockFloat) }
            .wasInvoked()
        verify { flowSettings.getBooleanFlow(KEY, mockBoolean) }
            .wasInvoked()
        verify { flowSettings.getIntFlow(KEY, mockInt) }
            .wasInvoked()
        verify { flowSettings.getStringFlow(KEY, mockString) }
            .wasInvoked()
        verify { flowSettings.getLongFlow(KEY, mockLong) }
            .wasInvoked()
    }
}
