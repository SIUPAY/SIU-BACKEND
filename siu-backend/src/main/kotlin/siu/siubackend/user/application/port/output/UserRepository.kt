package siu.siubackend.user.application.port.output

import siu.siubackend.user.domain.User
import java.util.UUID

interface UserRepository {
    fun findByOauthUserId(oauthUserId: String): User?
    fun findByIdentifier(identifier: UUID): User?
    fun save(user: User): User
}
