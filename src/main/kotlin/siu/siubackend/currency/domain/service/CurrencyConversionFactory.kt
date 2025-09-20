package siu.siubackend.currency.domain.service

import org.springframework.stereotype.Component
import siu.siubackend.currency.domain.CurrencyConversionRequest
import siu.siubackend.currency.domain.CurrencyConversionResult

@Component
object CurrencyConversionFactory {
    
    fun createConversionRequest(fiatAmount: Double): CurrencyConversionRequest {
        return CurrencyConversionRequest(fiatAmount = fiatAmount)
    }
    
    fun createConversionResult(cryptoAmount: Double): CurrencyConversionResult {
        return CurrencyConversionResult(cryptoAmount = cryptoAmount)
    }
}
