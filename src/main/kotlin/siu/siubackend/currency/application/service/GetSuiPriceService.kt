package siu.siubackend.currency.application.service

import org.springframework.stereotype.Service
import siu.siubackend.currency.application.port.`in`.GetSuiPriceUseCase
import siu.siubackend.currency.application.port.out.MarketDataPort
import siu.siubackend.currency.domain.CryptoTicker

@Service
class GetSuiPriceService(
    private val marketDataPort: MarketDataPort
) : GetSuiPriceUseCase {
    
    override fun getSuiPrice(): CryptoTicker {
        return marketDataPort.getSuiPrice()
    }
}
