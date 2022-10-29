package com.oztechan.ccc.backend.util

import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates
import com.oztechan.ccc.test.BaseTest
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

internal class ApiUtilTest : BaseTest() {

    @Test
    fun fillMissingRatesWith() {
        val first = CurrencyResponse("", null, Rates())
        val second = CurrencyResponse(
            "",
            null,
            Rates(
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

        first.fillMissingRatesWith(second)

        assertEquals(second.rates.btc, first.rates.btc)
        assertEquals(second.rates.clf, first.rates.clf)
        assertEquals(second.rates.cnh, first.rates.cnh)
        assertEquals(second.rates.jep, first.rates.jep)
        assertEquals(second.rates.kpw, first.rates.kpw)
        assertEquals(second.rates.mro, first.rates.mro)
        assertEquals(second.rates.std, first.rates.std)
        assertEquals(second.rates.svc, first.rates.svc)
        assertEquals(second.rates.xag, first.rates.xag)
        assertEquals(second.rates.xau, first.rates.xau)
        assertEquals(second.rates.xpd, first.rates.xpd)
        assertEquals(second.rates.xpt, first.rates.xpt)
        assertEquals(second.rates.zwl, first.rates.zwl)
    }
}
