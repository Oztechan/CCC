package com.github.mustafaozhan.ccc.common.db.offlinerates

import com.github.mustafaozhan.ccc.common.db.sql.OfflineRatesQueries
import com.github.mustafaozhan.ccc.common.entity.toCurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.model.toModel
import com.github.mustafaozhan.logmob.kermit

internal class OfflineRatesRepositoryImpl(
    private val offlineRatesQueries: OfflineRatesQueries
) : OfflineRatesRepository {

    override fun insertOfflineRates(rates: Rates) = with(rates) {
        offlineRatesQueries.insertOfflineRates(
            base, date, aED, aFN, aLL, aMD, aNG, aOA, aRS, aUD, aWG, aZN, bAM, bBD, bDT, bGN,
            bHD, bIF, bMD, bND, bOB, bRL, bSD, bTC, bTN, bWP, bYN, bZD, cAD, cDF, cHF, cLF,
            cLP, cNH, cNY, cOP, cRC, cUC, cUP, cVE, cZK, dJF, dKK, dOP, dZD, eGP, eRN, eTB,
            eUR, fJD, fKP, gBP, gEL, gGP, gHS, gIP, gMD, gNF, gTQ, gYD, hKD, hNL, hRK, hTG,
            hUF, iDR, iLS, iMP, iNR, iQD, iRR, iSK, jEP, jMD, jOD, jPY, kES, kGS, kHR, kMF,
            kPW, kRW, kWD, kYD, kZT, lAK, lBP, lKR, lRD, lSL, lYD, mAD, mDL, mGA, mKD, mMK,
            mNT, mOP, mRO, mRU, mUR, mVR, mWK, mXN, mYR, mZN, nAD, nGN, nIO, nOK, nPR, nZD,
            oMR, pAB, pEN, pGK, pHP, pKR, pLN, pYG, qAR, rON, rSD, rUB, rWF, sAR, sBD, sCR,
            sDG, sEK, sGD, sHP, sLL, sOS, sRD, sSP, sTD, sTN, sVC, sYP, sZL, tHB, tJS, tMT,
            tND, tOP, tRY, tTD, tWD, tZS, uAH, uGX, uSD, uYU, uZS, vES, vND, vUV, wST, xAF,
            xAG, xAU, xCD, xDR, xOF, xPD, xPF, xPT, yER, zAR, zMW, zWL
        ).also { kermit.d { "OfflineRatesRepositoryImpl insertOfflineRates ${rates.base}" } }
    }

    override fun getOfflineRatesByBase(baseName: String) = offlineRatesQueries
        .getOfflineRatesByBase(baseName)
        .executeAsOneOrNull()
        ?.toModel()
        .also { kermit.d { "OfflineRatesRepositoryImpl getOfflineRatesByBase $baseName" } }

    override fun getOfflineCurrencyResponseByBase(baseName: String) = offlineRatesQueries
        .getOfflineRatesByBase(baseName)
        .executeAsOneOrNull()
        ?.toCurrencyResponseEntity()
        .also { kermit.d { "OfflineRatesRepositoryImpl getOfflineCurrencyResponseByBase $baseName" } }
}
