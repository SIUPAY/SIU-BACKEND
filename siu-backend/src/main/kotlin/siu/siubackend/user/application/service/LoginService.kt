package siu.siubackend.user.application.service

import org.springframework.stereotype.Service
import siu.siubackend.user.adapter.input.web.dto.LoginRequest
import siu.siubackend.user.adapter.input.web.dto.LoginType
import siu.siubackend.user.application.port.input.LoginUseCase
import siu.siubackend.user.application.port.output.UserRepository
import siu.siubackend.user.domain.User
import siu.siubackend.user.domain.service.UserFactory

@Service
class LoginService(
    private val userRepository: UserRepository
) : LoginUseCase {

    override fun processLogin(request: LoginRequest): User {
        return when (request.loginType) {
            LoginType.ZKLOGIN -> processZkLogin(request.oauthUserId!!)
            LoginType.WALLET -> processWalletLogin(request.walletAddress!!)
        }
    }

    private fun processZkLogin(oauthUserId: String): User {
        return userRepository.findByOauthUserId(oauthUserId)
            ?: createNewUserForZkLogin(oauthUserId)
    }

    private fun processWalletLogin(walletAddress: String): User {
        return userRepository.findByWalletAddress(walletAddress)
            ?: createNewUserForWallet(walletAddress)
    }

    private fun createNewUserForZkLogin(oauthUserId: String): User {
        val user = UserFactory.createUserForZkLogin(oauthUserId)
        return userRepository.save(user)
    }

    private fun createNewUserForWallet(walletAddress: String): User {
        val user = UserFactory.createUserForWallet(walletAddress)
        return userRepository.save(user)
    }
}
