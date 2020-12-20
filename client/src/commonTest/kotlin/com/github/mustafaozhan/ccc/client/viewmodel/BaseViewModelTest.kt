/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.common.Currency
import com.github.mustafaozhan.ccc.common.CurrencyQueries
import com.github.mustafaozhan.ccc.common.OfflineRatesQueries
import com.github.mustafaozhan.ccc.common.Offline_rates
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn

abstract class BaseViewModelTest<ViewModelType> : CurrencyQueries, OfflineRatesQueries, Settings {
    protected abstract var viewModel: ViewModelType

    override fun <T : Any> collectAllCurrencies(
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun collectAllCurrencies(): Query<Currency> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> collectActiveCurrencies(
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun collectActiveCurrencies(): Query<Currency> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> getActiveCurrencies(
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun getActiveCurrencies(): Query<Currency> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> getCurrencyByName(
        name: String,
        mapper: (name: String, longName: String, symbol: String, rate: Double, isActive: Long) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun getCurrencyByName(name: String): Query<Currency> {
        TODO("Not yet implemented")
    }

    override fun updateCurrencyStateByName(isActive: Long, name: String) {
        TODO("Not yet implemented")
    }

    override fun updateAllCurrencyState(isActive: Long) {
        TODO("Not yet implemented")
    }

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun <R> transactionWithResult(
        noEnclosing: Boolean,
        bodyWithReturn: TransactionWithReturn<R>.() -> R
    ): R {
        TODO("Not yet implemented")
    }

    override val keys: Set<String>
        get() = TODO("Not yet implemented")
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBooleanOrNull(key: String): Boolean? {
        TODO("Not yet implemented")
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        TODO("Not yet implemented")
    }

    override fun getDoubleOrNull(key: String): Double? {
        TODO("Not yet implemented")
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        TODO("Not yet implemented")
    }

    override fun getFloatOrNull(key: String): Float? {
        TODO("Not yet implemented")
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return 1
    }

    override fun getIntOrNull(key: String): Int? {
        // todo faked
        TODO("Not yet implemented")
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        // todo faked
        return 1.toLong()
    }

    override fun getLongOrNull(key: String): Long? {
        TODO("Not yet implemented")
    }

    override fun getString(key: String, defaultValue: String): String {
        TODO("Not yet implemented")
    }

    override fun getStringOrNull(key: String): String? {
        TODO("Not yet implemented")
    }

    override fun hasKey(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun putBoolean(key: String, value: Boolean) {
        TODO("Not yet implemented")
    }

    override fun putDouble(key: String, value: Double) {
        TODO("Not yet implemented")
    }

    override fun putFloat(key: String, value: Float) {
        TODO("Not yet implemented")
    }

    override fun putInt(key: String, value: Int) {
        TODO("Not yet implemented")
    }

    override fun putLong(key: String, value: Long) {
        // todo faked
    }

    override fun putString(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> getOfflineRatesByBase(
        base: String,
        mapper: (
            base: String,
            date: String?,
            AED: Double?,
            AFN: Double?,
            ALLL: Double?,
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
            BYR: Double?,
            BZD: Double?,
            CAD: Double?,
            CDF: Double?,
            CHF: Double?,
            CLF: Double?,
            CLP: Double?,
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
            LTL: Double?,
            LVL: Double?,
            LYD: Double?,
            MAD: Double?,
            MDL: Double?,
            MGA: Double?,
            MKD: Double?,
            MMK: Double?,
            MNT: Double?,
            MOP: Double?,
            MRO: Double?,
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
            STD: Double?,
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
            VEF: Double?,
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
            XPF: Double?,
            YER: Double?,
            ZAR: Double?,
            ZMK: Double?,
            ZMW: Double?,
            ZWL: Double?
        ) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun getOfflineRatesByBase(base: String): Query<Offline_rates> {
        TODO("Not yet implemented")
    }

    override fun insertOfflineRates(
        base: String,
        date: String?,
        AED: Double?,
        AFN: Double?,
        ALLL: Double?,
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
        BYR: Double?,
        BZD: Double?,
        CAD: Double?,
        CDF: Double?,
        CHF: Double?,
        CLF: Double?,
        CLP: Double?,
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
        LTL: Double?,
        LVL: Double?,
        LYD: Double?,
        MAD: Double?,
        MDL: Double?,
        MGA: Double?,
        MKD: Double?,
        MMK: Double?,
        MNT: Double?,
        MOP: Double?,
        MRO: Double?,
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
        STD: Double?,
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
        VEF: Double?,
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
        XPF: Double?,
        YER: Double?,
        ZAR: Double?,
        ZMK: Double?,
        ZMW: Double?,
        ZWL: Double?
    ) {
        TODO("Not yet implemented")
    }
}
