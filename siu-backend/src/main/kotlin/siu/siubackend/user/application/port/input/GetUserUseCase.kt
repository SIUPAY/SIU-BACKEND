package siu.siubackend.user.application.port.input

import siu.siubackend.user.domain.User
import java.util.UUID

interface GetUserUseCase {
    fun getUser(userIdentifier: UUID): User
}
