package siu.siubackend.zkloginsalt.adapter.`in`.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.zkloginsalt.application.port.`in`.GenerateZkLoginSaltUseCase

@RestController
@RequestMapping("/api/v1/auth")
class ZkLoginSaltController(
    private val generateZkLoginSaltUseCase: GenerateZkLoginSaltUseCase
) {

    @PostMapping("/salt")
    fun generateSalt(
        @RequestBody request: ZkLoginSaltRequest
    ): ResponseEntity<ZkLoginSaltResponse> {
        val command = GenerateZkLoginSaltUseCase.Command(
            oauthUserId = request.oauthUserId
        )
        
        val salt = generateZkLoginSaltUseCase.generateSalt(command)
        
        return ResponseEntity.ok(ZkLoginSaltResponse(zkloginSalt = salt))
    }
}
