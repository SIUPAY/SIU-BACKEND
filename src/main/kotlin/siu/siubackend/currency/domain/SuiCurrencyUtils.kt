package siu.siubackend.currency.domain

import java.math.BigDecimal
import java.math.RoundingMode

object SuiCurrencyUtils {
    private const val MIST_PER_SUI = 1_000_000_000L
    
    fun mistToSui(mistAmount: Double): Double {
        return BigDecimal.valueOf(mistAmount)
            .divide(BigDecimal.valueOf(MIST_PER_SUI), 9, RoundingMode.HALF_UP)
            .toDouble()
    }
    
    fun suiToMist(suiAmount: Double): Long {
        return BigDecimal.valueOf(suiAmount)
            .multiply(BigDecimal.valueOf(MIST_PER_SUI))
            .setScale(0, RoundingMode.HALF_UP)
            .toLong()
    }
}
