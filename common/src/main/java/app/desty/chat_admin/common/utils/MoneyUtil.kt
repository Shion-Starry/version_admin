package app.desty.chat_admin.common.utils

import app.desty.chat_admin.common.enum_bean.MoneyType
import java.lang.Exception
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object MoneyUtil {
    private val idNumberFormat = NumberFormat.getInstance(Locale("id")) as DecimalFormat

    fun formatMoney(money: Any?, moneyType: MoneyType = MoneyType.Rupiah): String {
        val amount = toBigDecimal(money)
        return "${moneyType.unit} ${idNumberFormat.format(amount)}"
    }

    fun formatRangeMoney(
        minMoney: Any?,
        maxMoney: Any?,
        moneyType: MoneyType = MoneyType.Rupiah
                        ): String {
        val minAmount = toBigDecimal(minMoney)
        val maxAmount = toBigDecimal(maxMoney)
        return "${moneyType.unit} ${idNumberFormat.format(minAmount)}-${
            idNumberFormat.format(
                maxAmount
                                 )
        }"
    }

    fun formatSmartMoney(
        minMoney: Any?,
        maxMoney: Any?,
        moneyType: MoneyType = MoneyType.Rupiah
                        ): String {
        val minAmount = toBigDecimal(minMoney)
        val maxAmount = toBigDecimal(maxMoney)
        return if (minAmount.compareTo(maxAmount) == 0) {
            "${moneyType.unit} ${idNumberFormat.format(minAmount)}"
        } else {
            "${moneyType.unit} ${idNumberFormat.format(minAmount)}-${idNumberFormat.format(maxAmount)}"
        }

    }


    private fun toBigDecimal(money: Any?): BigDecimal {
        return try {
            when (money) {
                is String     -> BigDecimal(money)
                is Int        -> BigDecimal(money)
                is Long       -> BigDecimal(money)
                is Double     -> BigDecimal(money)
                is BigInteger -> BigDecimal(money)
                else          -> BigDecimal("0")
            }
        } catch (e: Exception) {
            BigDecimal("0")
        }
    }
}