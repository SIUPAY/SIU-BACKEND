package siu.siubackend.currency.application.port.`in`

import siu.siubackend.currency.domain.CurrencyConversionResult

interface ConvertCurrencyUseCase {
    fun convertKrwToSui(fiatAmount: Double): CurrencyConversionResult
}
