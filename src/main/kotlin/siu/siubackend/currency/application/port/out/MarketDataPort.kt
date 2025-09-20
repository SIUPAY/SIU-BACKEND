package siu.siubackend.currency.application.port.out

import siu.siubackend.currency.domain.CryptoTicker

interface MarketDataPort {
    fun getSuiPrice(): CryptoTicker
}
