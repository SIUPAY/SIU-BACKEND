package siu.siubackend.user.adapter.input.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.user.adapter.input.web.dto.WalletLoginRequest
import siu.siubackend.user.adapter.input.web.dto.WalletLoginResponse
import siu.siubackend.user.application.port.input.WalletLoginUseCase

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val walletLoginUseCase: WalletLoginUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody request: WalletLoginRequest): ResponseEntity<WalletLoginResponse> {
        val command = WalletLoginUseCase.Command(
            walletAddress = request.walletAddress
        )
        
        val result = walletLoginUseCase.login(command)
        
        val response = WalletLoginResponse(
            identifier = result.identifier,
            walletAddress = result.walletAddress,
            storeIdentifier = result.storeIdentifier
        )
        
        return ResponseEntity.ok(response)
    }
}
