package siu.siubackend.user.application.service

import org.springframework.stereotype.Service
import siu.siubackend.user.application.port.input.ZkLoginUseCase
import siu.siubackend.user.application.port.output.UserRepository
import siu.siubackend.user.domain.User
import siu.siubackend.user.domain.service.UserFactory

@Service
class ZkLoginService(
    private val userRepository: UserRepository
) : ZkLoginUseCase {

    override fun processZkLogin(oauthUserId: String): User {
        return userRepository.findByOauthUserId(oauthUserId)
            ?: createNewUser(oauthUserId)
    }

    private fun createNewUser(oauthUserId: String): User {
        val user = UserFactory.createUserForZkLogin(oauthUserId)
        return userRepository.save(user)
    }
}
