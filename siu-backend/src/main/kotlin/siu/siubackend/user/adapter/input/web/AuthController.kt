package siu.siubackend.user.adapter.input.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.user.adapter.input.web.dto.LoginRequest
import siu.siubackend.user.adapter.input.web.dto.LoginResponse
import siu.siubackend.user.adapter.input.web.dto.LoginType
import siu.siubackend.user.application.port.input.LoginUseCase

@RestController
@RequestMapping("/api/auth")
class  AuthController(
    private val loginUseCase: LoginUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        validateRequest(request)
        
        val user = loginUseCase.processLogin(request)
        
        val response = LoginResponse(
            identifier = user.identifier,
            zkloginSalt = user.zkloginSalt,
            walletAddress = user.walletAddress,
            storeIdentifier = user.storeIdentifier
        )
        
        return ResponseEntity.ok(response)
    }

    private fun validateRequest(request: LoginRequest) {
        when (request.loginType) {
            LoginType.ZKLOGIN -> {
                require(!request.oauthUserId.isNullOrBlank()) {
                    "oauth_user_id is required for ZKLOGIN type"
                }
            }
            LoginType.WALLET -> {
                require(!request.walletAddress.isNullOrBlank()) {
                    "wallet_address is required for WALLET type"
                }
            }
        }
    }
}
