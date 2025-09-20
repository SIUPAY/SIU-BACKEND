package siu.siubackend.currency.adapter.`in`.web

import com.fasterxml.jackson.annotation.JsonProperty

data class SuiPriceResponse(
    @JsonProperty("current_price")
    val currentPrice: Double
)
