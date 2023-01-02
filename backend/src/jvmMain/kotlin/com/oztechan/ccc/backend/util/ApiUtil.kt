package com.oztechan.ccc.backend.util

import com.oztechan.ccc.common.model.CurrencyResponse

internal fun CurrencyResponse.fillMissingConversionWith(
    nonPremiumResponse: CurrencyResponse
) = apply {
    conversion = conversion.copy(
        btc = nonPremiumResponse.conversion.btc,
        clf = nonPremiumResponse.conversion.clf,
        cnh = nonPremiumResponse.conversion.cnh,
        jep = nonPremiumResponse.conversion.jep,
        kpw = nonPremiumResponse.conversion.kpw,
        mro = nonPremiumResponse.conversion.mro,
        std = nonPremiumResponse.conversion.std,
        svc = nonPremiumResponse.conversion.svc,
        xag = nonPremiumResponse.conversion.xag,
        xau = nonPremiumResponse.conversion.xau,
        xpd = nonPremiumResponse.conversion.xpd,
        xpt = nonPremiumResponse.conversion.xpt,
        zwl = nonPremiumResponse.conversion.zwl
    )
}
