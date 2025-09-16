package siu.siubackend.user.application.port.output

import siu.siubackend.user.domain.User

interface UserRepository {
    fun findByOauthUserId(oauthUserId: String): User?
    fun save(user: User): User
}
