package siu.siubackend.user.application.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import siu.siubackend.user.adapter.input.web.dto.LoginRequest
import siu.siubackend.user.adapter.input.web.dto.LoginType
import siu.siubackend.user.application.port.input.LoginUseCase
import siu.siubackend.user.application.port.output.UserRepository
import siu.siubackend.user.domain.User
import siu.siubackend.user.domain.service.UserFactory

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val userFactory: UserFactory
) : LoginUseCase {

    private val logger = LoggerFactory.getLogger(LoginService::class.java)

    override fun processLogin(request: LoginRequest): User {
        logger.debug("Processing login for type: {}, wallet_address: {}", request.login_type, request.wallet_address)
        
        return when (request.login_type) {
            LoginType.ZKLOGIN -> processZkLogin(request.oauth_user_id!!, request.wallet_address)
            LoginType.WALLET -> processWalletLogin(request.wallet_address)
        }
    }

    private fun processZkLogin(oauthUserId: String, walletAddress: String): User {
        logger.debug("Processing ZK login for oauth_user_id: {}, wallet_address: {}", oauthUserId, walletAddress)
        
        val existingUserByOauth = userRepository.findByOauthUserId(oauthUserId)
        
        return when {
            existingUserByOauth != null -> {
                if (existingUserByOauth.walletAddress.isNullOrBlank()) {
                    logger.info("Updating wallet address for existing user: {} with new wallet: {}", existingUserByOauth.identifier, walletAddress)
                    val updatedUser = existingUserByOauth.updateWalletAddress(walletAddress)
                    userRepository.save(updatedUser)
                } else {
                    logger.debug("Found existing user with oauth_user_id: {}", oauthUserId)
                    existingUserByOauth
                }
            }
            else -> {
                logger.debug("No user found with oauth_user_id: {}, checking wallet_address", oauthUserId)
                userRepository.findByWalletAddress(walletAddress)
                    ?: createNewUserForZkLogin(oauthUserId, walletAddress)
            }
        }
    }

    private fun processWalletLogin(walletAddress: String): User {
        logger.debug("Processing wallet login for wallet_address: {}", walletAddress)
        
        return userRepository.findByWalletAddress(walletAddress)
            ?: createNewUserForWallet(walletAddress)
    }

    private fun createNewUserForZkLogin(oauthUserId: String, walletAddress: String): User {
        logger.debug("Creating new user for ZK login: oauth_user_id={}, wallet_address={}", oauthUserId, walletAddress)
        
        val user = userFactory.createUserForZkLogin(oauthUserId, walletAddress)
        val savedUser = userRepository.save(user)
        
        logger.info("New ZK login user created with identifier: {}", savedUser.identifier)
        return savedUser
    }

    private fun createNewUserForWallet(walletAddress: String): User {
        logger.debug("Creating new user for wallet login: wallet_address={}", walletAddress)
        
        val user = userFactory.createUserForWallet(walletAddress)
        val savedUser = userRepository.save(user)
        
        logger.info("New wallet user created with identifier: {}", savedUser.identifier)
        return savedUser
    }
}
