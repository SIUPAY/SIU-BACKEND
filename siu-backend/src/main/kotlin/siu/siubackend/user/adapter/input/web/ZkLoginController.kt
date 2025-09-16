package siu.siubackend.user.adapter.input.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.user.adapter.input.web.dto.ZkLoginRequest
import siu.siubackend.user.adapter.input.web.dto.ZkLoginResponse
import siu.siubackend.user.application.port.input.ZkLoginUseCase

@RestController
@RequestMapping("/api/auth")
class ZkLoginController(
    private val zkLoginUseCase: ZkLoginUseCase
) {

    @PostMapping("/zklogin")
    fun zkLogin(@RequestBody request: ZkLoginRequest): ResponseEntity<ZkLoginResponse> {
        val user = zkLoginUseCase.processZkLogin(request.oauthUserId)
        
        val response = ZkLoginResponse(
            identifier = user.identifier,
            zkloginSalt = user.zkloginSalt ?: throw IllegalStateException("ZKLogin salt is missing")
        )
        
        return ResponseEntity.ok(response)
    }
}
