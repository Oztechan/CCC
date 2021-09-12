/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.fake

import com.github.mustafaozhan.ccc.common.db.sql.OfflineRatesQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import com.github.mustafaozhan.ccc.common.db.sql.Offline_rates as OfflineRates

@Suppress("StringLiteralDuplication", "UNCHECKED_CAST")
object FakeOfflineRatesQueries : OfflineRatesQueries {

    private val FAKE_OFFLINE_RATES = OfflineRates(
        "EUR", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )

    fun getOfflineRatesQueries(): OfflineRatesQueries = this

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) = Unit

    override fun <R> transactionWithResult(
        noEnclosing: Boolean,
        bodyWithReturn: TransactionWithReturn<R>.() -> R
    ): R {
        TODO("Fake method Not yet implemented")
    }

    override fun <T : Any> getOfflineRatesByBase(
        base: String,
        mapper: (
            base: String,
            date: String?,
            AED: Double?,
            AFN: Double?,
            ALL: Double?,
            AMD: Double?,
            ANG: Double?,
            AOA: Double?,
            ARS: Double?,
            AUD: Double?,
            AWG: Double?,
            AZN: Double?,
            BAM: Double?,
            BBD: Double?,
            BDT: Double?,
            BGN: Double?,
            BHD: Double?,
            BIF: Double?,
            BMD: Double?,
            BND: Double?,
            BOB: Double?,
            BRL: Double?,
            BSD: Double?,
            BTC: Double?,
            BTN: Double?,
            BWP: Double?,
            BYN: Double?,
            BZD: Double?,
            CAD: Double?,
            CDF: Double?,
            CHF: Double?,
            CLF: Double?,
            CLP: Double?,
            CNH: Double?,
            CNY: Double?,
            COP: Double?,
            CRC: Double?,
            CUC: Double?,
            CUP: Double?,
            CVE: Double?,
            CZK: Double?,
            DJF: Double?,
            DKK: Double?,
            DOP: Double?,
            DZD: Double?,
            EGP: Double?,
            ERN: Double?,
            ETB: Double?,
            EUR: Double?,
            FJD: Double?,
            FKP: Double?,
            GBP: Double?,
            GEL: Double?,
            GGP: Double?,
            GHS: Double?,
            GIP: Double?,
            GMD: Double?,
            GNF: Double?,
            GTQ: Double?,
            GYD: Double?,
            HKD: Double?,
            HNL: Double?,
            HRK: Double?,
            HTG: Double?,
            HUF: Double?,
            IDR: Double?,
            ILS: Double?,
            IMP: Double?,
            INR: Double?,
            IQD: Double?,
            IRR: Double?,
            ISK: Double?,
            JEP: Double?,
            JMD: Double?,
            JOD: Double?,
            JPY: Double?,
            KES: Double?,
            KGS: Double?,
            KHR: Double?,
            KMF: Double?,
            KPW: Double?,
            KRW: Double?,
            KWD: Double?,
            KYD: Double?,
            KZT: Double?,
            LAK: Double?,
            LBP: Double?,
            LKR: Double?,
            LRD: Double?,
            LSL: Double?,
            LYD: Double?,
            MAD: Double?,
            MDL: Double?,
            MGA: Double?,
            MKD: Double?,
            MMK: Double?,
            MNT: Double?,
            MOP: Double?,
            MRO: Double?,
            MRU: Double?,
            MUR: Double?,
            MVR: Double?,
            MWK: Double?,
            MXN: Double?,
            MYR: Double?,
            MZN: Double?,
            NAD: Double?,
            NGN: Double?,
            NIO: Double?,
            NOK: Double?,
            NPR: Double?,
            NZD: Double?,
            OMR: Double?,
            PAB: Double?,
            PEN: Double?,
            PGK: Double?,
            PHP: Double?,
            PKR: Double?,
            PLN: Double?,
            PYG: Double?,
            QAR: Double?,
            RON: Double?,
            RSD: Double?,
            RUB: Double?,
            RWF: Double?,
            SAR: Double?,
            SBD: Double?,
            SCR: Double?,
            SDG: Double?,
            SEK: Double?,
            SGD: Double?,
            SHP: Double?,
            SLL: Double?,
            SOS: Double?,
            SRD: Double?,
            SSP: Double?,
            STD: Double?,
            STN: Double?,
            SVC: Double?,
            SYP: Double?,
            SZL: Double?,
            THB: Double?,
            TJS: Double?,
            TMT: Double?,
            TND: Double?,
            TOP: Double?,
            TRY: Double?,
            TTD: Double?,
            TWD: Double?,
            TZS: Double?,
            UAH: Double?,
            UGX: Double?,
            USD: Double?,
            UYU: Double?,
            UZS: Double?,
            VES: Double?,
            VND: Double?,
            VUV: Double?,
            WST: Double?,
            XAF: Double?,
            XAG: Double?,
            XAU: Double?,
            XCD: Double?,
            XDR: Double?,
            XOF: Double?,
            XPD: Double?,
            XPF: Double?,
            XPT: Double?,
            YER: Double?,
            ZAR: Double?,
            ZMW: Double?,
            ZWL: Double?
        ) -> T
    ) = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_OFFLINE_RATES
    } as Query<T>

    override fun getOfflineRatesByBase(base: String): Query<OfflineRates> = Query(
        -1,
        mutableListOf(),
        FakeDriver.getDriver(),
        "query"
    ) {
        FAKE_OFFLINE_RATES
    }

    override fun insertOfflineRates(
        offline_rates: OfflineRates
    ) = Unit
}
