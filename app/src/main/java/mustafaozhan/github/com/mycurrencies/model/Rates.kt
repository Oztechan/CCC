package mustafaozhan.github.com.mycurrencies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
@Entity(tableName = "offline_rates")
data class Rates(
    @PrimaryKey @ColumnInfo(name = "base") var base: String,
    @SerializedName("AED", alternate = ["aed"]) @ColumnInfo(name = "AED")
    var aED: Double? = null,
    @SerializedName("AFN", alternate = ["afn"]) @ColumnInfo(name = "AFN")
    var aFN: Double? = null,
    @SerializedName("ALL", alternate = ["all"]) @ColumnInfo(name = "ALLL")
    var aLL: Double? = null,
    @SerializedName("AMD", alternate = ["amd"]) @ColumnInfo(name = "AMD")
    var aMD: Double? = null,
    @SerializedName("ANG", alternate = ["ang"]) @ColumnInfo(name = "ANG")
    var aNG: Double? = null,
    @SerializedName("AOA", alternate = ["aoa"]) @ColumnInfo(name = "AOA")
    var aOA: Double? = null,
    @SerializedName("ARS", alternate = ["ars"]) @ColumnInfo(name = "ARS")
    var aRS: Double? = null,
    @SerializedName("AUD", alternate = ["aud"]) @ColumnInfo(name = "AUD")
    var aUD: Double? = null,
    @SerializedName("AWG", alternate = ["awg"]) @ColumnInfo(name = "AWG")
    var aWG: Double? = null,
    @SerializedName("AZN", alternate = ["azn"]) @ColumnInfo(name = "AZN")
    var aZN: Double? = null,
    @SerializedName("BAM", alternate = ["bam"]) @ColumnInfo(name = "BAM")
    var bAM: Double? = null,
    @SerializedName("BBD", alternate = ["bbd"]) @ColumnInfo(name = "BBD")
    var bBD: Double? = null,
    @SerializedName("BDT", alternate = ["bdt"]) @ColumnInfo(name = "BDT")
    var bDT: Double? = null,
    @SerializedName("BGN", alternate = ["bgn"]) @ColumnInfo(name = "BGN")
    var bGN: Double? = null,
    @SerializedName("BHD", alternate = ["bhd"]) @ColumnInfo(name = "BHD")
    var bHD: Double? = null,
    @SerializedName("BIF", alternate = ["bif"]) @ColumnInfo(name = "BIF")
    var bIF: Double? = null,
    @SerializedName("BMD", alternate = ["bmd"]) @ColumnInfo(name = "BMD")
    var bMD: Double? = null,
    @SerializedName("BND", alternate = ["bnd"]) @ColumnInfo(name = "BND")
    var bND: Double? = null,
    @SerializedName("BOB", alternate = ["bob"]) @ColumnInfo(name = "BOB")
    var bOB: Double? = null,
    @SerializedName("BRL", alternate = ["brl"]) @ColumnInfo(name = "BRL")
    var bRL: Double? = null,
    @SerializedName("BSD", alternate = ["bsd"]) @ColumnInfo(name = "BSD")
    var bSD: Double? = null,
    @SerializedName("CRYPTO_BTC", alternate = ["btc"]) @ColumnInfo(name = "BTC")
    var bTC: Double? = null,
    @SerializedName("BTN", alternate = ["btn"]) @ColumnInfo(name = "BTN")
    var bTN: Double? = null,
    @SerializedName("BWP", alternate = ["bwp"]) @ColumnInfo(name = "BWP")
    var bWP: Double? = null,
    @SerializedName("BYN", alternate = ["byn"]) @ColumnInfo(name = "BYN")
    var bYN: Double? = null,
    @SerializedName("BYR", alternate = ["byr"]) @ColumnInfo(name = "BYR")
    var bYR: Double? = null,
    @SerializedName("BZD", alternate = ["bzd"]) @ColumnInfo(name = "BZD")
    var bZD: Double? = null,
    @SerializedName("CAD", alternate = ["cad"]) @ColumnInfo(name = "CAD")
    var cAD: Double? = null,
    @SerializedName("CDF", alternate = ["cdf"]) @ColumnInfo(name = "CDF")
    var cDF: Double? = null,
    @SerializedName("CHF", alternate = ["chf"]) @ColumnInfo(name = "CHF")
    var cHF: Double? = null,
    @SerializedName("CLF", alternate = ["clf"]) @ColumnInfo(name = "CLF")
    var cLF: Double? = null,
    @SerializedName("CLP", alternate = ["clp"]) @ColumnInfo(name = "CLP")
    var cLP: Double? = null,
    @SerializedName("CNY", alternate = ["cny"]) @ColumnInfo(name = "CNY")
    var cNY: Double? = null,
    @SerializedName("COP", alternate = ["cop"]) @ColumnInfo(name = "COP")
    var cOP: Double? = null,
    @SerializedName("CRC", alternate = ["crc"]) @ColumnInfo(name = "CRC")
    var cRC: Double? = null,
    @SerializedName("CUC", alternate = ["cuc"]) @ColumnInfo(name = "CUC")
    var cUC: Double? = null,
    @SerializedName("CUP", alternate = ["cup"]) @ColumnInfo(name = "CUP")
    var cUP: Double? = null,
    @SerializedName("CVE", alternate = ["cve"]) @ColumnInfo(name = "CVE")
    var cVE: Double? = null,
    @SerializedName("CZK", alternate = ["czk"]) @ColumnInfo(name = "CZK")
    var cZK: Double? = null,
    @SerializedName("DJF", alternate = ["djf"]) @ColumnInfo(name = "DJF")
    var dJF: Double? = null,
    @SerializedName("DKK", alternate = ["dkk"]) @ColumnInfo(name = "DKK")
    var dKK: Double? = null,
    @SerializedName("DOP", alternate = ["dop"]) @ColumnInfo(name = "DOP")
    var dOP: Double? = null,
    @SerializedName("DZD", alternate = ["dzd"]) @ColumnInfo(name = "DZD")
    var dZD: Double? = null,
    @SerializedName("EGP", alternate = ["egp"]) @ColumnInfo(name = "EGP")
    var eGP: Double? = null,
    @SerializedName("ERN", alternate = ["ern"]) @ColumnInfo(name = "ERN")
    var eRN: Double? = null,
    @SerializedName("ETB", alternate = ["etb"]) @ColumnInfo(name = "ETB")
    var eTB: Double? = null,
    @SerializedName("EUR", alternate = ["eur"]) @ColumnInfo(name = "EUR")
    var eUR: Double? = null,
    @SerializedName("FJD", alternate = ["fjd"]) @ColumnInfo(name = "FJD")
    var fJD: Double? = null,
    @SerializedName("FKP", alternate = ["fkp"]) @ColumnInfo(name = "FKP")
    var fKP: Double? = null,
    @SerializedName("GBP", alternate = ["gbp"]) @ColumnInfo(name = "GBP")
    var gBP: Double? = null,
    @SerializedName("GEL", alternate = ["gel"]) @ColumnInfo(name = "GEL")
    var gEL: Double? = null,
    @SerializedName("GGP", alternate = ["ggp"]) @ColumnInfo(name = "GGP")
    var gGP: Double? = null,
    @SerializedName("GHS", alternate = ["ghs"]) @ColumnInfo(name = "GHS")
    var gHS: Double? = null,
    @SerializedName("GIP", alternate = ["gip"]) @ColumnInfo(name = "GIP")
    var gIP: Double? = null,
    @SerializedName("GMD", alternate = ["gmd"]) @ColumnInfo(name = "GMD")
    var gMD: Double? = null,
    @SerializedName("GNF", alternate = ["gnf"]) @ColumnInfo(name = "GNF")
    var gNF: Double? = null,
    @SerializedName("GTQ", alternate = ["gtq"]) @ColumnInfo(name = "GTQ")
    var gTQ: Double? = null,
    @SerializedName("GYD", alternate = ["gyd"]) @ColumnInfo(name = "GYD")
    var gYD: Double? = null,
    @SerializedName("HKD", alternate = ["hkd"]) @ColumnInfo(name = "HKD")
    var hKD: Double? = null,
    @SerializedName("HNL", alternate = ["hnl"]) @ColumnInfo(name = "HNL")
    var hNL: Double? = null,
    @SerializedName("HRK", alternate = ["hrk"]) @ColumnInfo(name = "HRK")
    var hRK: Double? = null,
    @SerializedName("HTG", alternate = ["htg"]) @ColumnInfo(name = "HTG")
    var hTG: Double? = null,
    @SerializedName("HUF", alternate = ["huf"]) @ColumnInfo(name = "HUF")
    var hUF: Double? = null,
    @SerializedName("IDR", alternate = ["idr"]) @ColumnInfo(name = "IDR")
    var iDR: Double? = null,
    @SerializedName("ILS", alternate = ["ils"]) @ColumnInfo(name = "ILS")
    var iLS: Double? = null,
    @SerializedName("IMP", alternate = ["imp"]) @ColumnInfo(name = "IMP")
    var iMP: Double? = null,
    @SerializedName("INR", alternate = ["inr"]) @ColumnInfo(name = "INR")
    var iNR: Double? = null,
    @SerializedName("IQD", alternate = ["iqd"]) @ColumnInfo(name = "IQD")
    var iQD: Double? = null,
    @SerializedName("IRR", alternate = ["irr"]) @ColumnInfo(name = "IRR")
    var iRR: Double? = null,
    @SerializedName("ISK", alternate = ["isk"]) @ColumnInfo(name = "ISK")
    var iSK: Double? = null,
    @SerializedName("JEP", alternate = ["jep"]) @ColumnInfo(name = "JEP")
    var jEP: Double? = null,
    @SerializedName("JMD", alternate = ["jmd"]) @ColumnInfo(name = "JMD")
    var jMD: Double? = null,
    @SerializedName("JOD", alternate = ["jod"]) @ColumnInfo(name = "JOD")
    var jOD: Double? = null,
    @SerializedName("JPY", alternate = ["jpy"]) @ColumnInfo(name = "JPY")
    var jPY: Double? = null,
    @SerializedName("KES", alternate = ["kes"]) @ColumnInfo(name = "KES")
    var kES: Double? = null,
    @SerializedName("KGS", alternate = ["kgs"]) @ColumnInfo(name = "KGS")
    var kGS: Double? = null,
    @SerializedName("KHR", alternate = ["khr"]) @ColumnInfo(name = "KHR")
    var kHR: Double? = null,
    @SerializedName("KMF", alternate = ["kmf"]) @ColumnInfo(name = "KMF")
    var kMF: Double? = null,
    @SerializedName("KPW", alternate = ["kpw"]) @ColumnInfo(name = "KPW")
    var kPW: Double? = null,
    @SerializedName("KRW", alternate = ["krw"]) @ColumnInfo(name = "KRW")
    var kRW: Double? = null,
    @SerializedName("KWD", alternate = ["kwd"]) @ColumnInfo(name = "KWD")
    var kWD: Double? = null,
    @SerializedName("KYD", alternate = ["kyd"]) @ColumnInfo(name = "KYD")
    var kYD: Double? = null,
    @SerializedName("KZT", alternate = ["kzt"]) @ColumnInfo(name = "KZT")
    var kZT: Double? = null,
    @SerializedName("LAK", alternate = ["lak"]) @ColumnInfo(name = "LAK")
    var lAK: Double? = null,
    @SerializedName("LBP", alternate = ["lbp"]) @ColumnInfo(name = "LBP")
    var lBP: Double? = null,
    @SerializedName("LKR", alternate = ["lkr"]) @ColumnInfo(name = "LKR")
    var lKR: Double? = null,
    @SerializedName("LRD", alternate = ["lrd"]) @ColumnInfo(name = "LRD")
    var lRD: Double? = null,
    @SerializedName("LSL", alternate = ["lsl"]) @ColumnInfo(name = "LSL")
    var lSL: Double? = null,
    @SerializedName("LTL", alternate = ["ltl"]) @ColumnInfo(name = "LTL")
    var lTL: Double? = null,
    @SerializedName("LVL", alternate = ["lvl"]) @ColumnInfo(name = "LVL")
    var lVL: Double? = null,
    @SerializedName("LYD", alternate = ["lyd"]) @ColumnInfo(name = "LYD")
    var lYD: Double? = null,
    @SerializedName("MAD", alternate = ["mad"]) @ColumnInfo(name = "MAD")
    var mAD: Double? = null,
    @SerializedName("MDL", alternate = ["mdl"]) @ColumnInfo(name = "MDL")
    var mDL: Double? = null,
    @SerializedName("MGA", alternate = ["mga"]) @ColumnInfo(name = "MGA")
    var mGA: Double? = null,
    @SerializedName("MKD", alternate = ["mkd"]) @ColumnInfo(name = "MKD")
    var mKD: Double? = null,
    @SerializedName("MMK", alternate = ["mmk"]) @ColumnInfo(name = "MMK")
    var mMK: Double? = null,
    @SerializedName("MNT", alternate = ["mnt"]) @ColumnInfo(name = "MNT")
    var mNT: Double? = null,
    @SerializedName("MOP", alternate = ["mop"]) @ColumnInfo(name = "MOP")
    var mOP: Double? = null,
    @SerializedName("MRO", alternate = ["mro"]) @ColumnInfo(name = "MRO")
    var mRO: Double? = null,
    @SerializedName("MUR", alternate = ["mur"]) @ColumnInfo(name = "MUR")
    var mUR: Double? = null,
    @SerializedName("MVR", alternate = ["mvr"]) @ColumnInfo(name = "MVR")
    var mVR: Double? = null,
    @SerializedName("MWK", alternate = ["mwk"]) @ColumnInfo(name = "MWK")
    var mWK: Double? = null,
    @SerializedName("MXN", alternate = ["mxn"]) @ColumnInfo(name = "MXN")
    var mXN: Double? = null,
    @SerializedName("MYR", alternate = ["myr"]) @ColumnInfo(name = "MYR")
    var mYR: Double? = null,
    @SerializedName("MZN", alternate = ["mzn"]) @ColumnInfo(name = "MZN")
    var mZN: Double? = null,
    @SerializedName("NAD", alternate = ["nad"]) @ColumnInfo(name = "NAD")
    var nAD: Double? = null,
    @SerializedName("NGN", alternate = ["ngn"]) @ColumnInfo(name = "NGN")
    var nGN: Double? = null,
    @SerializedName("NIO", alternate = ["nio"]) @ColumnInfo(name = "NIO")
    var nIO: Double? = null,
    @SerializedName("NOK", alternate = ["nok"]) @ColumnInfo(name = "NOK")
    var nOK: Double? = null,
    @SerializedName("NPR", alternate = ["npr"]) @ColumnInfo(name = "NPR")
    var nPR: Double? = null,
    @SerializedName("NZD", alternate = ["nzd"]) @ColumnInfo(name = "NZD")
    var nZD: Double? = null,
    @SerializedName("OMR", alternate = ["omr"]) @ColumnInfo(name = "OMR")
    var oMR: Double? = null,
    @SerializedName("PAB", alternate = ["pab"]) @ColumnInfo(name = "PAB")
    var pAB: Double? = null,
    @SerializedName("PEN", alternate = ["pen"]) @ColumnInfo(name = "PEN")
    var pEN: Double? = null,
    @SerializedName("PGK", alternate = ["pgk"]) @ColumnInfo(name = "PGK")
    var pGK: Double? = null,
    @SerializedName("PHP", alternate = ["php"]) @ColumnInfo(name = "PHP")
    var pHP: Double? = null,
    @SerializedName("PKR", alternate = ["pkr"]) @ColumnInfo(name = "PKR")
    var pKR: Double? = null,
    @SerializedName("PLN", alternate = ["pln"]) @ColumnInfo(name = "PLN")
    var pLN: Double? = null,
    @SerializedName("PYG", alternate = ["pyg"]) @ColumnInfo(name = "PYG")
    var pYG: Double? = null,
    @SerializedName("QAR", alternate = ["qar"]) @ColumnInfo(name = "QAR")
    var qAR: Double? = null,
    @SerializedName("RON", alternate = ["ron"]) @ColumnInfo(name = "RON")
    var rON: Double? = null,
    @SerializedName("RSD", alternate = ["rsd"]) @ColumnInfo(name = "RSD")
    var rSD: Double? = null,
    @SerializedName("RUB", alternate = ["rub"]) @ColumnInfo(name = "RUB")
    var rUB: Double? = null,
    @SerializedName("RWF", alternate = ["rwf"]) @ColumnInfo(name = "RWF")
    var rWF: Double? = null,
    @SerializedName("SAR", alternate = ["sar"]) @ColumnInfo(name = "SAR")
    var sAR: Double? = null,
    @SerializedName("SBD", alternate = ["sbd"]) @ColumnInfo(name = "SBD")
    var sBD: Double? = null,
    @SerializedName("SCR", alternate = ["scr"]) @ColumnInfo(name = "SCR")
    var sCR: Double? = null,
    @SerializedName("SDG", alternate = ["sdg"]) @ColumnInfo(name = "SDG")
    var sDG: Double? = null,
    @SerializedName("SEK", alternate = ["sek"]) @ColumnInfo(name = "SEK")
    var sEK: Double? = null,
    @SerializedName("SGD", alternate = ["sgd"]) @ColumnInfo(name = "SGD")
    var sGD: Double? = null,
    @SerializedName("SHP", alternate = ["shp"]) @ColumnInfo(name = "SHP")
    var sHP: Double? = null,
    @SerializedName("SLL", alternate = ["sll"]) @ColumnInfo(name = "SLL")
    var sLL: Double? = null,
    @SerializedName("SOS", alternate = ["sos"]) @ColumnInfo(name = "SOS")
    var sOS: Double? = null,
    @SerializedName("SRD", alternate = ["srd"]) @ColumnInfo(name = "SRD")
    var sRD: Double? = null,
    @SerializedName("STD", alternate = ["std"]) @ColumnInfo(name = "STD")
    var sTD: Double? = null,
    @SerializedName("SVC", alternate = ["svc"]) @ColumnInfo(name = "SVC")
    var sVC: Double? = null,
    @SerializedName("SYP", alternate = ["syp"]) @ColumnInfo(name = "SYP")
    var sYP: Double? = null,
    @SerializedName("SZL", alternate = ["szl"]) @ColumnInfo(name = "SZL")
    var sZL: Double? = null,
    @SerializedName("THB", alternate = ["thb"]) @ColumnInfo(name = "THB")
    var tHB: Double? = null,
    @SerializedName("TJS", alternate = ["tjs"]) @ColumnInfo(name = "TJS")
    var tJS: Double? = null,
    @SerializedName("TMT", alternate = ["tmt"]) @ColumnInfo(name = "TMT")
    var tMT: Double? = null,
    @SerializedName("TND", alternate = ["tnd"]) @ColumnInfo(name = "TND")
    var tND: Double? = null,
    @SerializedName("TOP", alternate = ["top"]) @ColumnInfo(name = "TOP")
    var tOP: Double? = null,
    @SerializedName("TRY", alternate = ["try"]) @ColumnInfo(name = "TRY")
    var tRY: Double? = null,
    @SerializedName("TTD", alternate = ["ttd"]) @ColumnInfo(name = "TTD")
    var tTD: Double? = null,
    @SerializedName("TWD", alternate = ["twd"]) @ColumnInfo(name = "TWD")
    var tWD: Double? = null,
    @SerializedName("TZS", alternate = ["tzs"]) @ColumnInfo(name = "TZS")
    var tZS: Double? = null,
    @SerializedName("UAH", alternate = ["uah"]) @ColumnInfo(name = "UAH")
    var uAH: Double? = null,
    @SerializedName("UGX", alternate = ["ugx"]) @ColumnInfo(name = "UGX")
    var uGX: Double? = null,
    @SerializedName("USD", alternate = ["usd"]) @ColumnInfo(name = "USD")
    var uSD: Double? = null,
    @SerializedName("UYU", alternate = ["uyu"]) @ColumnInfo(name = "UYU")
    var uYU: Double? = null,
    @SerializedName("UZS", alternate = ["uzs"]) @ColumnInfo(name = "UZS")
    var uZS: Double? = null,
    @SerializedName("VEF", alternate = ["vef"]) @ColumnInfo(name = "VEF")
    var vEF: Double? = null,
    @SerializedName("VES", alternate = ["ves"]) @ColumnInfo(name = "VES")
    var vES: Double? = null,
    @SerializedName("VND", alternate = ["vnd"]) @ColumnInfo(name = "VND")
    var vND: Double? = null,
    @SerializedName("VUV", alternate = ["vuv"]) @ColumnInfo(name = "VUV")
    var vUV: Double? = null,
    @SerializedName("WST", alternate = ["wst"]) @ColumnInfo(name = "WST")
    var wST: Double? = null,
    @SerializedName("XAF", alternate = ["xaf"]) @ColumnInfo(name = "XAF")
    var xAF: Double? = null,
    @SerializedName("XAG", alternate = ["xag"]) @ColumnInfo(name = "XAG")
    var xAG: Double? = null,
    @SerializedName("XAU", alternate = ["xau"]) @ColumnInfo(name = "XAU")
    var xAU: Double? = null,
    @SerializedName("XCD", alternate = ["xcd"]) @ColumnInfo(name = "XCD")
    var xCD: Double? = null,
    @SerializedName("XDR", alternate = ["xdr"]) @ColumnInfo(name = "XDR")
    var xDR: Double? = null,
    @SerializedName("XOF", alternate = ["xof"]) @ColumnInfo(name = "XOF")
    var xOF: Double? = null,
    @SerializedName("XPF", alternate = ["xpf"]) @ColumnInfo(name = "XPF")
    var xPF: Double? = null,
    @SerializedName("YER", alternate = ["yer"]) @ColumnInfo(name = "YER")
    var yER: Double? = null,
    @SerializedName("ZAR", alternate = ["zar"]) @ColumnInfo(name = "ZAR")
    var zAR: Double? = null,
    @SerializedName("ZMK", alternate = ["zmk"]) @ColumnInfo(name = "ZMK")
    var zMK: Double? = null,
    @SerializedName("ZMW", alternate = ["zmw"]) @ColumnInfo(name = "ZMW")
    var zMW: Double? = null,
    @SerializedName("ZWL", alternate = ["zwl"]) @ColumnInfo(name = "ZWL")
    var zWL: Double? = null
)
