package siu.siubackend.currency.adapter.`in`.web

import jakarta.validation.constraints.Positive

data class CurrencyConvertRequest(
    @field:Positive(message = "fiat_amount must be positive")
    val fiatAmount: Double
)
