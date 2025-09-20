package siu.siubackend.currency.adapter.`in`.web

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import siu.siubackend.currency.application.port.`in`.ConvertCurrencyUseCase
import siu.siubackend.currency.application.port.`in`.GetSuiPriceUseCase

@RestController
@RequestMapping("/api/v1/currency")
class CurrencyController(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val getSuiPriceUseCase: GetSuiPriceUseCase
) {
    
    @PostMapping("/convert")
    fun convertCurrency(
        @Valid @RequestBody request: CurrencyConvertRequest
    ): ResponseEntity<CurrencyConvertResponse> {
        val result = convertCurrencyUseCase.convertKrwToSui(request.fiatAmount)
        
        val response = CurrencyConvertResponse(
            cryptoAmount = result.cryptoAmount
        )
        
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/sui")
    fun getSuiPrice(): ResponseEntity<SuiPriceResponse> {
        val suiTicker = getSuiPriceUseCase.getSuiPrice()
        
        val response = SuiPriceResponse(
            currentPrice = suiTicker.tradePrice
        )
        
        return ResponseEntity.ok(response)
    }
}
