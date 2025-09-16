package siu.siubackend.user.application.port.input

import siu.siubackend.user.domain.User

interface ZkLoginUseCase {
    fun processZkLogin(oauthUserId: String): User
}
