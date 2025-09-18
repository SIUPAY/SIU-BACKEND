package siu.siubackend.user.application.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import siu.siubackend.user.application.port.input.GenerateSaltUseCase
import siu.siubackend.user.application.port.output.UserRepository
import siu.siubackend.user.domain.User
import siu.siubackend.user.domain.service.UserFactory

@Service
class GenerateSaltService(
    private val userRepository: UserRepository,
    private val userFactory: UserFactory
) : GenerateSaltUseCase {

    private val logger = LoggerFactory.getLogger(GenerateSaltService::class.java)

    override fun generateSalt(oauthUserId: String): User {
        logger.debug("Generating salt for oauth_user_id: {}", oauthUserId)
        
        val existingUser = userRepository.findByOauthUserId(oauthUserId)
        
        return existingUser ?: run {
            logger.debug("Creating new user for oauth_user_id: {}", oauthUserId)
            val newUser = userFactory.createWithOauthUserId(oauthUserId)
            val savedUser = userRepository.save(newUser)
            logger.info("New user created with identifier: {}", savedUser.identifier)
            savedUser
        }
    }
}
