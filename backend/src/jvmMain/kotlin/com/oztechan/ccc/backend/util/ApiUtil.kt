package com.oztechan.ccc.backend.util

import com.oztechan.ccc.common.model.CurrencyResponse

internal fun CurrencyResponse.fillMissingRatesWith(
    nonPremiumResponse: CurrencyResponse
) = apply {
    rates = rates.copy(
        btc = nonPremiumResponse.rates.btc,
        clf = nonPremiumResponse.rates.clf,
        cnh = nonPremiumResponse.rates.cnh,
        jep = nonPremiumResponse.rates.jep,
        kpw = nonPremiumResponse.rates.kpw,
        mro = nonPremiumResponse.rates.mro,
        std = nonPremiumResponse.rates.std,
        svc = nonPremiumResponse.rates.svc,
        xag = nonPremiumResponse.rates.xag,
        xau = nonPremiumResponse.rates.xau,
        xpd = nonPremiumResponse.rates.xpd,
        xpt = nonPremiumResponse.rates.xpt,
        zwl = nonPremiumResponse.rates.zwl
    )
}
