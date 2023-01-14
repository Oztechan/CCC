package com.oztechan.ccc.backend.util

import com.oztechan.ccc.common.model.Conversion
import com.oztechan.ccc.common.model.ExchangeRate
import com.oztechan.ccc.test.BaseTest
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

internal class ApiUtilTest : BaseTest() {

    @Test
    fun fillMissingConversionWith() {
        val first = ExchangeRate("", null, Conversion())
        val second = ExchangeRate(
            "",
            null,
            Conversion(
                btc = Random.nextDouble(),
                clf = Random.nextDouble(),
                cnh = Random.nextDouble(),
                jep = Random.nextDouble(),
                kpw = Random.nextDouble(),
                mro = Random.nextDouble(),
                std = Random.nextDouble(),
                svc = Random.nextDouble(),
                xag = Random.nextDouble(),
                xau = Random.nextDouble(),
                xpd = Random.nextDouble(),
                xpt = Random.nextDouble(),
                zwl = Random.nextDouble()
            )
        )

        first.fillMissingConversionWith(second)

        assertEquals(second.conversion.btc, first.conversion.btc)
        assertEquals(second.conversion.clf, first.conversion.clf)
        assertEquals(second.conversion.cnh, first.conversion.cnh)
        assertEquals(second.conversion.jep, first.conversion.jep)
        assertEquals(second.conversion.kpw, first.conversion.kpw)
        assertEquals(second.conversion.mro, first.conversion.mro)
        assertEquals(second.conversion.std, first.conversion.std)
        assertEquals(second.conversion.svc, first.conversion.svc)
        assertEquals(second.conversion.xag, first.conversion.xag)
        assertEquals(second.conversion.xau, first.conversion.xau)
        assertEquals(second.conversion.xpd, first.conversion.xpd)
        assertEquals(second.conversion.xpt, first.conversion.xpt)
        assertEquals(second.conversion.zwl, first.conversion.zwl)
    }
}
