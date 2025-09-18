package siu.siubackend.user.application.port.input

import siu.siubackend.user.domain.User

interface GenerateSaltUseCase {
    fun generateSalt(oauthUserId: String): User
}
