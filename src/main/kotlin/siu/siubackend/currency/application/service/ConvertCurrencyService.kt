package siu.siubackend.currency.application.service

import org.springframework.stereotype.Service
import siu.siubackend.currency.application.port.`in`.ConvertCurrencyUseCase
import siu.siubackend.currency.application.port.out.MarketDataPort
import siu.siubackend.currency.domain.CurrencyConversionResult
import siu.siubackend.currency.domain.service.CurrencyConversionFactory

@Service
class ConvertCurrencyService(
    private val marketDataPort: MarketDataPort
) : ConvertCurrencyUseCase {
    
    override fun convertKrwToSui(fiatAmount: Double): CurrencyConversionResult {
        val suiTicker = marketDataPort.getSuiPrice()
        val cryptoAmount = fiatAmount / suiTicker.tradePrice
        
        return CurrencyConversionFactory.createConversionResult(cryptoAmount)
    }
}
