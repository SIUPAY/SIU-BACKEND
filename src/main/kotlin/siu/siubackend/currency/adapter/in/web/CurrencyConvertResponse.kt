package siu.siubackend.currency.adapter.`in`.web

import com.fasterxml.jackson.annotation.JsonProperty

data class CurrencyConvertResponse(
    @JsonProperty("crypto_amount")
    val cryptoAmount: Double
)
