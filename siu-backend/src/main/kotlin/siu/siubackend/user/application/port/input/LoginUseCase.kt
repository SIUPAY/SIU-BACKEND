package siu.siubackend.user.application.port.input

import siu.siubackend.user.adapter.input.web.dto.LoginRequest
import siu.siubackend.user.domain.User

interface LoginUseCase {
    fun processLogin(request: LoginRequest): User
}
