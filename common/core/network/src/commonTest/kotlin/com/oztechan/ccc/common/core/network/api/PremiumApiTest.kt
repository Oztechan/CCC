package com.oztechan.ccc.common.core.network.api

import com.oztechan.ccc.common.core.network.api.premium.PremiumApi
import com.oztechan.ccc.common.core.network.api.premium.PremiumApiImpl
import com.oztechan.ccc.common.core.network.di.setupHttpClientConfig
import com.oztechan.ccc.common.core.network.fakes.Fakes
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class PremiumApiTest {
    @Suppress("LongMethod")
    @Test
    fun getExchangeRateSuccess() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(Json.encodeToString(Fakes.exchangeRate)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val apiClient = PremiumApiImpl(
            HttpClient(mockEngine) {
                setupHttpClientConfig()
            }
        ) as PremiumApi

        apiClient.getExchangeRate(Fakes.BASE).apply {
            assertEquals(Fakes.BASE, base)
            assertEquals(Fakes.DATE, date)
            assertEquals(Fakes.exchangeRate.base, conversion.base)
            assertEquals(Fakes.exchangeRate.conversion.aed, conversion.aed)
            assertEquals(Fakes.exchangeRate.conversion.afn, conversion.afn)
            assertEquals(Fakes.exchangeRate.conversion.all, conversion.all)
            assertEquals(Fakes.exchangeRate.conversion.amd, conversion.amd)
            assertEquals(Fakes.exchangeRate.conversion.ang, conversion.ang)
            assertEquals(Fakes.exchangeRate.conversion.aoa, conversion.aoa)
            assertEquals(Fakes.exchangeRate.conversion.ars, conversion.ars)
            assertEquals(Fakes.exchangeRate.conversion.aud, conversion.aud)
            assertEquals(Fakes.exchangeRate.conversion.awg, conversion.awg)
            assertEquals(Fakes.exchangeRate.conversion.azn, conversion.azn)
            assertEquals(Fakes.exchangeRate.conversion.bam, conversion.bam)
            assertEquals(Fakes.exchangeRate.conversion.bbd, conversion.bbd)
            assertEquals(Fakes.exchangeRate.conversion.bdt, conversion.bdt)
            assertEquals(Fakes.exchangeRate.conversion.bgn, conversion.bgn)
            assertEquals(Fakes.exchangeRate.conversion.bhd, conversion.bhd)
            assertEquals(Fakes.exchangeRate.conversion.bif, conversion.bif)
            assertEquals(Fakes.exchangeRate.conversion.bmd, conversion.bmd)
            assertEquals(Fakes.exchangeRate.conversion.bnd, conversion.bnd)
            assertEquals(Fakes.exchangeRate.conversion.bob, conversion.bob)
            assertEquals(Fakes.exchangeRate.conversion.brl, conversion.brl)
            assertEquals(Fakes.exchangeRate.conversion.bsd, conversion.bsd)
            assertEquals(Fakes.exchangeRate.conversion.btn, conversion.btn)
            assertEquals(Fakes.exchangeRate.conversion.bwp, conversion.bwp)
            assertEquals(Fakes.exchangeRate.conversion.byn, conversion.byn)
            assertEquals(Fakes.exchangeRate.conversion.bzd, conversion.bzd)
            assertEquals(Fakes.exchangeRate.conversion.cad, conversion.cad)
            assertEquals(Fakes.exchangeRate.conversion.cdf, conversion.cdf)
            assertEquals(Fakes.exchangeRate.conversion.chf, conversion.chf)
            assertEquals(Fakes.exchangeRate.conversion.clp, conversion.clp)
            assertEquals(Fakes.exchangeRate.conversion.cny, conversion.cny)
            assertEquals(Fakes.exchangeRate.conversion.cop, conversion.cop)
            assertEquals(Fakes.exchangeRate.conversion.crc, conversion.crc)
            assertEquals(Fakes.exchangeRate.conversion.cup, conversion.cup)
            assertEquals(Fakes.exchangeRate.conversion.cve, conversion.cve)
            assertEquals(Fakes.exchangeRate.conversion.czk, conversion.czk)
            assertEquals(Fakes.exchangeRate.conversion.djf, conversion.djf)
            assertEquals(Fakes.exchangeRate.conversion.dkk, conversion.dkk)
            assertEquals(Fakes.exchangeRate.conversion.dop, conversion.dop)
            assertEquals(Fakes.exchangeRate.conversion.dzd, conversion.dzd)
            assertEquals(Fakes.exchangeRate.conversion.egp, conversion.egp)
            assertEquals(Fakes.exchangeRate.conversion.ern, conversion.ern)
            assertEquals(Fakes.exchangeRate.conversion.etb, conversion.etb)
            assertEquals(Fakes.exchangeRate.conversion.eur, conversion.eur)
            assertEquals(Fakes.exchangeRate.conversion.fjd, conversion.fjd)
            assertEquals(Fakes.exchangeRate.conversion.fkp, conversion.fkp)
            assertEquals(Fakes.exchangeRate.conversion.fok, conversion.fok)
            assertEquals(Fakes.exchangeRate.conversion.gbp, conversion.gbp)
            assertEquals(Fakes.exchangeRate.conversion.gel, conversion.gel)
            assertEquals(Fakes.exchangeRate.conversion.ggp, conversion.ggp)
            assertEquals(Fakes.exchangeRate.conversion.ghs, conversion.ghs)
            assertEquals(Fakes.exchangeRate.conversion.gip, conversion.gip)
            assertEquals(Fakes.exchangeRate.conversion.gmd, conversion.gmd)
            assertEquals(Fakes.exchangeRate.conversion.gnf, conversion.gnf)
            assertEquals(Fakes.exchangeRate.conversion.gtq, conversion.gtq)
            assertEquals(Fakes.exchangeRate.conversion.gyd, conversion.gyd)
            assertEquals(Fakes.exchangeRate.conversion.hkd, conversion.hkd)
            assertEquals(Fakes.exchangeRate.conversion.hnl, conversion.hnl)
            assertEquals(Fakes.exchangeRate.conversion.hrk, conversion.hrk)
            assertEquals(Fakes.exchangeRate.conversion.htg, conversion.htg)
            assertEquals(Fakes.exchangeRate.conversion.huf, conversion.huf)
            assertEquals(Fakes.exchangeRate.conversion.idr, conversion.idr)
            assertEquals(Fakes.exchangeRate.conversion.ils, conversion.ils)
            assertEquals(Fakes.exchangeRate.conversion.imp, conversion.imp)
            assertEquals(Fakes.exchangeRate.conversion.inr, conversion.inr)
            assertEquals(Fakes.exchangeRate.conversion.iqd, conversion.iqd)
            assertEquals(Fakes.exchangeRate.conversion.irr, conversion.irr)
            assertEquals(Fakes.exchangeRate.conversion.isk, conversion.isk)
            assertEquals(Fakes.exchangeRate.conversion.jep, conversion.jep)
            assertEquals(Fakes.exchangeRate.conversion.jmd, conversion.jmd)
            assertEquals(Fakes.exchangeRate.conversion.jod, conversion.jod)
            assertEquals(Fakes.exchangeRate.conversion.jpy, conversion.jpy)
            assertEquals(Fakes.exchangeRate.conversion.kes, conversion.kes)
            assertEquals(Fakes.exchangeRate.conversion.kgs, conversion.kgs)
            assertEquals(Fakes.exchangeRate.conversion.khr, conversion.khr)
            assertEquals(Fakes.exchangeRate.conversion.kid, conversion.kid)
            assertEquals(Fakes.exchangeRate.conversion.kmf, conversion.kmf)
            assertEquals(Fakes.exchangeRate.conversion.krw, conversion.krw)
            assertEquals(Fakes.exchangeRate.conversion.kwd, conversion.kwd)
            assertEquals(Fakes.exchangeRate.conversion.kyd, conversion.kyd)
            assertEquals(Fakes.exchangeRate.conversion.kzt, conversion.kzt)
            assertEquals(Fakes.exchangeRate.conversion.lak, conversion.lak)
            assertEquals(Fakes.exchangeRate.conversion.lbp, conversion.lbp)
            assertEquals(Fakes.exchangeRate.conversion.lkr, conversion.lkr)
            assertEquals(Fakes.exchangeRate.conversion.lrd, conversion.lrd)
            assertEquals(Fakes.exchangeRate.conversion.lsl, conversion.lsl)
            assertEquals(Fakes.exchangeRate.conversion.lyd, conversion.lyd)
            assertEquals(Fakes.exchangeRate.conversion.mad, conversion.mad)
            assertEquals(Fakes.exchangeRate.conversion.mdl, conversion.mdl)
            assertEquals(Fakes.exchangeRate.conversion.mga, conversion.mga)
            assertEquals(Fakes.exchangeRate.conversion.mkd, conversion.mkd)
            assertEquals(Fakes.exchangeRate.conversion.mmk, conversion.mmk)
            assertEquals(Fakes.exchangeRate.conversion.mnt, conversion.mnt)
            assertEquals(Fakes.exchangeRate.conversion.mop, conversion.mop)
            assertEquals(Fakes.exchangeRate.conversion.mru, conversion.mru)
            assertEquals(Fakes.exchangeRate.conversion.mur, conversion.mur)
            assertEquals(Fakes.exchangeRate.conversion.mvr, conversion.mvr)
            assertEquals(Fakes.exchangeRate.conversion.mwk, conversion.mwk)
            assertEquals(Fakes.exchangeRate.conversion.mxn, conversion.mxn)
            assertEquals(Fakes.exchangeRate.conversion.myr, conversion.myr)
            assertEquals(Fakes.exchangeRate.conversion.mzn, conversion.mzn)
            assertEquals(Fakes.exchangeRate.conversion.nad, conversion.nad)
            assertEquals(Fakes.exchangeRate.conversion.ngn, conversion.ngn)
            assertEquals(Fakes.exchangeRate.conversion.nio, conversion.nio)
            assertEquals(Fakes.exchangeRate.conversion.nok, conversion.nok)
            assertEquals(Fakes.exchangeRate.conversion.npr, conversion.npr)
            assertEquals(Fakes.exchangeRate.conversion.nzd, conversion.nzd)
            assertEquals(Fakes.exchangeRate.conversion.omr, conversion.omr)
            assertEquals(Fakes.exchangeRate.conversion.pab, conversion.pab)
            assertEquals(Fakes.exchangeRate.conversion.pen, conversion.pen)
            assertEquals(Fakes.exchangeRate.conversion.pgk, conversion.pgk)
            assertEquals(Fakes.exchangeRate.conversion.php, conversion.php)
            assertEquals(Fakes.exchangeRate.conversion.pkr, conversion.pkr)
            assertEquals(Fakes.exchangeRate.conversion.pln, conversion.pln)
            assertEquals(Fakes.exchangeRate.conversion.pyg, conversion.pyg)
            assertEquals(Fakes.exchangeRate.conversion.qar, conversion.qar)
            assertEquals(Fakes.exchangeRate.conversion.ron, conversion.ron)
            assertEquals(Fakes.exchangeRate.conversion.rsd, conversion.rsd)
            assertEquals(Fakes.exchangeRate.conversion.rub, conversion.rub)
            assertEquals(Fakes.exchangeRate.conversion.rwf, conversion.rwf)
            assertEquals(Fakes.exchangeRate.conversion.sar, conversion.sar)
            assertEquals(Fakes.exchangeRate.conversion.sbd, conversion.sbd)
            assertEquals(Fakes.exchangeRate.conversion.scr, conversion.scr)
            assertEquals(Fakes.exchangeRate.conversion.sdg, conversion.sdg)
            assertEquals(Fakes.exchangeRate.conversion.sek, conversion.sek)
            assertEquals(Fakes.exchangeRate.conversion.sgd, conversion.sgd)
            assertEquals(Fakes.exchangeRate.conversion.shp, conversion.shp)
            assertEquals(Fakes.exchangeRate.conversion.sle, conversion.sle)
            assertEquals(Fakes.exchangeRate.conversion.sll, conversion.sll)
            assertEquals(Fakes.exchangeRate.conversion.sos, conversion.sos)
            assertEquals(Fakes.exchangeRate.conversion.srd, conversion.srd)
            assertEquals(Fakes.exchangeRate.conversion.ssp, conversion.ssp)
            assertEquals(Fakes.exchangeRate.conversion.stn, conversion.stn)
            assertEquals(Fakes.exchangeRate.conversion.syp, conversion.syp)
            assertEquals(Fakes.exchangeRate.conversion.szl, conversion.szl)
            assertEquals(Fakes.exchangeRate.conversion.thb, conversion.thb)
            assertEquals(Fakes.exchangeRate.conversion.tjs, conversion.tjs)
            assertEquals(Fakes.exchangeRate.conversion.tmt, conversion.tmt)
            assertEquals(Fakes.exchangeRate.conversion.tnd, conversion.tnd)
            assertEquals(Fakes.exchangeRate.conversion.top, conversion.top)
            assertEquals(Fakes.exchangeRate.conversion.`try`, conversion.`try`)
            assertEquals(Fakes.exchangeRate.conversion.ttd, conversion.ttd)
            assertEquals(Fakes.exchangeRate.conversion.tvd, conversion.tvd)
            assertEquals(Fakes.exchangeRate.conversion.twd, conversion.twd)
            assertEquals(Fakes.exchangeRate.conversion.tzs, conversion.tzs)
            assertEquals(Fakes.exchangeRate.conversion.uah, conversion.uah)
            assertEquals(Fakes.exchangeRate.conversion.ugx, conversion.ugx)
            assertEquals(Fakes.exchangeRate.conversion.usd, conversion.usd)
            assertEquals(Fakes.exchangeRate.conversion.uyu, conversion.uyu)
            assertEquals(Fakes.exchangeRate.conversion.uzs, conversion.uzs)
            assertEquals(Fakes.exchangeRate.conversion.ves, conversion.ves)
            assertEquals(Fakes.exchangeRate.conversion.vnd, conversion.vnd)
            assertEquals(Fakes.exchangeRate.conversion.vuv, conversion.vuv)
            assertEquals(Fakes.exchangeRate.conversion.wst, conversion.wst)
            assertEquals(Fakes.exchangeRate.conversion.xaf, conversion.xaf)
            assertEquals(Fakes.exchangeRate.conversion.xcd, conversion.xcd)
            assertEquals(Fakes.exchangeRate.conversion.xdr, conversion.xdr)
            assertEquals(Fakes.exchangeRate.conversion.xof, conversion.xof)
            assertEquals(Fakes.exchangeRate.conversion.xpf, conversion.xpf)
            assertEquals(Fakes.exchangeRate.conversion.yer, conversion.yer)
            assertEquals(Fakes.exchangeRate.conversion.zar, conversion.zar)
            assertEquals(Fakes.exchangeRate.conversion.zmw, conversion.zmw)
            assertEquals(Fakes.exchangeRate.conversion.zwl, conversion.zwl)
        }
    }

    @Test
    fun getExchangeRateBadRequest() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = "",
                status = HttpStatusCode.BadRequest
            )
        }

        val apiClient = PremiumApiImpl(
            HttpClient(mockEngine) {
                setupHttpClientConfig()
            }
        ) as PremiumApi

        assertFails { apiClient.getExchangeRate("EUR") }
    }
}
