package siu.siubackend.currency.adapter.out.http

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import siu.siubackend.currency.application.port.out.MarketDataPort
import siu.siubackend.currency.domain.CryptoTicker

@Component
class UpbitMarketDataAdapter(
    private val restTemplate: RestTemplate
) : MarketDataPort {
    
    private val upbitApiUrl = "https://api.upbit.com/v1/ticker"
    
    override fun getSuiPrice(): CryptoTicker {
        val url = "$upbitApiUrl?markets=KRW-SUI"
        
        val response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<UpbitTickerResponse>>() {}
        )
        
        val tickerData = response.body?.firstOrNull()
            ?: throw RuntimeException("Failed to get SUI price from market data provider")
        
        return CryptoTicker(
            market = tickerData.market,
            tradePrice = tickerData.tradePrice
        )
    }
}
