<<<<<<<< HEAD:client/repository/adcontrol/src/commonTest/kotlin/com/oztechan/ccc/client/repository/adcontrol/AdControlRepositoryTest.kt
package com.oztechan.ccc.client.repository.adcontrol
|||||||| parent of eaa04e68d ([Oztechan/CCC#1828] Create :client:repository:ad module):client/src/commonTest/kotlin/com/oztechan/ccc/client/repository/AdRepositoryTest.kt
package com.oztechan.ccc.client.repository
========
package com.oztechan.ccc.client.repository.ad
>>>>>>>> eaa04e68d ([Oztechan/CCC#1828] Create :client:repository:ad module):client/repository/ad/src/commonTest/kotlin/com/oztechan/ccc/client/repository/ad/AdRepositoryTest.kt

import com.github.submob.logmob.initTestLogger
import com.oztechan.ccc.client.configservice.ad.AdConfigService
import com.oztechan.ccc.client.configservice.ad.model.AdConfig
import com.oztechan.ccc.client.core.shared.util.nowAsLong
import com.oztechan.ccc.client.storage.app.AppStorage
import com.oztechan.ccc.common.core.infrastructure.constants.SECOND
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
<<<<<<<< HEAD:client/repository/adcontrol/src/commonTest/kotlin/com/oztechan/ccc/client/repository/adcontrol/AdControlRepositoryTest.kt
internal class AdControlRepositoryTest {
|||||||| parent of eaa04e68d ([Oztechan/CCC#1828] Create :client:repository:ad module):client/src/commonTest/kotlin/com/oztechan/ccc/client/repository/AdRepositoryTest.kt
internal class AdRepositoryTest : BaseSubjectTest<AdRepository>() {
========
internal class AdRepositoryTest {
>>>>>>>> eaa04e68d ([Oztechan/CCC#1828] Create :client:repository:ad module):client/repository/ad/src/commonTest/kotlin/com/oztechan/ccc/client/repository/ad/AdRepositoryTest.kt

<<<<<<<< HEAD:client/repository/adcontrol/src/commonTest/kotlin/com/oztechan/ccc/client/repository/adcontrol/AdControlRepositoryTest.kt
    private val subject: AdControlRepository by lazy {
        AdControlRepositoryImpl(appStorage, adConfigService)
|||||||| parent of eaa04e68d ([Oztechan/CCC#1828] Create :client:repository:ad module):client/src/commonTest/kotlin/com/oztechan/ccc/client/repository/AdRepositoryTest.kt
    override val subject: AdRepository by lazy {
        AdRepositoryImpl(appStorage, adConfigService)
========
    private val subject: AdRepository by lazy {
        AdRepositoryImpl(appStorage, adConfigService)
>>>>>>>> eaa04e68d ([Oztechan/CCC#1828] Create :client:repository:ad module):client/repository/ad/src/commonTest/kotlin/com/oztechan/ccc/client/repository/ad/AdRepositoryTest.kt
    }

    @Mock
    private val adConfigService = mock(classOf<AdConfigService>())

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    private var mockedSessionCount = Random.nextInt()

    @BeforeTest
<<<<<<<< HEAD:client/repository/adcontrol/src/commonTest/kotlin/com/oztechan/ccc/client/repository/adcontrol/AdControlRepositoryTest.kt
    fun setup() {
|||||||| parent of eaa04e68d ([Oztechan/CCC#1828] Create :client:repository:ad module):client/src/commonTest/kotlin/com/oztechan/ccc/client/repository/AdRepositoryTest.kt
    override fun setup() {
        super.setup()

========
    fun setup() {
        initTestLogger()

>>>>>>>> eaa04e68d ([Oztechan/CCC#1828] Create :client:repository:ad module):client/repository/ad/src/commonTest/kotlin/com/oztechan/ccc/client/repository/ad/AdRepositoryTest.kt
        given(adConfigService)
            .invocation { config }
            .thenReturn(AdConfig(mockedSessionCount, mockedSessionCount, 0L, 0L))
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun and not premiumExpired and sessionCount smaller than banner 000`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not premiumExpired + sessionCount smaller than banner 100`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + premiumExpired + sessionCount smaller than banner 010`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + not premiumExpired + sessionCount bigger than banner 001`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when firstRun + premiumExpired + sessionCount bigger than banner 011`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(true)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + not premiumExpired + sessionCount bigger than banner 101`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() + SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is false when not firstRun + premiumExpired + sessionCount smaller than banner 110`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount - 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertFalse { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowBannerAd is true when not firstRun + premiumExpired + sessionCount bigger than banner 111`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount + 1L)

        given(appStorage)
            .invocation { premiumEndDate }
            .thenReturn(nowAsLong() - SECOND)

        given(appStorage)
            .invocation { firstRun }
            .thenReturn(false)

        assertTrue { subject.shouldShowBannerAd() }

        verify(appStorage)
            .invocation { premiumEndDate }
            .wasInvoked()

        verify(appStorage)
            .invocation { firstRun }
            .wasInvoked()

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns true when session count bigger than remote`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() + 1)

        assertTrue { subject.shouldShowInterstitialAd() }

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun `shouldShowInterstitialAd returns false when session count smaller than remote`() {
        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockedSessionCount.toLong() - 1)

        assertFalse { subject.shouldShowInterstitialAd() }

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(adConfigService)
            .invocation { config }
            .wasInvoked()
    }
}
