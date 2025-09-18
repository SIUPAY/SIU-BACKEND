package siu.siubackend.user.adapter.input.web

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.user.adapter.input.web.dto.*
import siu.siubackend.user.application.port.input.GenerateSaltUseCase
import siu.siubackend.user.application.port.input.LoginUseCase

@RestController
@RequestMapping("/api/auth")
class  AuthController(
    private val loginUseCase: LoginUseCase,
    private val generateSaltUseCase: GenerateSaltUseCase
) {

    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/salt")
    fun generateSalt(@RequestBody request: ZkLoginRequest): ResponseEntity<ZkLoginResponse> {
        logger.info("Generate salt request received: {}", request)
        
        val user = generateSaltUseCase.generateSalt(request.oauth_user_id)
        
        val response = ZkLoginResponse(
            identifier = user.identifier,
            zklogin_salt = user.zkloginSalt!!
        )
        
        logger.info("Generate salt successful for user: {}", user.identifier)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        logger.info("Login request received: {}", request)
        
        validateRequest(request)
        
        val user = loginUseCase.processLogin(request)
        
        val response = LoginResponse(
            identifier = user.identifier,
            wallet_address = user.walletAddress!!,
            zklogin_salt = user.zkloginSalt,
            store_identifier = user.storeIdentifier
        )
        
        logger.info("Login successful for user: {}", user.identifier)
        return ResponseEntity.ok(response)
    }

    private fun validateRequest(request: LoginRequest) {
        require(!request.wallet_address.isBlank()) {
            "wallet_address is required"
        }
        
        when (request.login_type) {
            LoginType.ZKLOGIN -> {
                require(!request.oauth_user_id.isNullOrBlank()) {
                    "oauth_user_id is required for ZKLOGIN type"
                }
            }
            LoginType.WALLET -> {
                // wallet_address는 이미 위에서 검증
            }
        }
    }
}
