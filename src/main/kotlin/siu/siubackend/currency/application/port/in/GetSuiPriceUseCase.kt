package siu.siubackend.currency.application.port.`in`

import siu.siubackend.currency.domain.CryptoTicker

interface GetSuiPriceUseCase {
    fun getSuiPrice(): CryptoTicker
}
