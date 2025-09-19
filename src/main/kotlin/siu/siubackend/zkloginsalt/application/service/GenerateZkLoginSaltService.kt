package siu.siubackend.zkloginsalt.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import siu.siubackend.zkloginsalt.application.port.`in`.GenerateZkLoginSaltUseCase
import siu.siubackend.zkloginsalt.application.port.out.ZkLoginSaltRepository
import siu.siubackend.zkloginsalt.domain.service.ZkLoginSaltFactory
import java.security.SecureRandom
import java.util.*

@Service
@Transactional
class GenerateZkLoginSaltService(
    private val zkLoginSaltRepository: ZkLoginSaltRepository
) : GenerateZkLoginSaltUseCase {

    override fun generateSalt(command: GenerateZkLoginSaltUseCase.Command): String {
        val existingSalt = zkLoginSaltRepository.findByOauthUserId(command.oauthUserId)
        
        if (existingSalt != null) {
            return existingSalt.zkloginSalt
        }
        
        val salt = generateRandomSalt()
        val zkLoginSalt = ZkLoginSaltFactory.create(
            oauthUserId = command.oauthUserId,
            zkloginSalt = salt
        )
        
        zkLoginSaltRepository.save(zkLoginSalt)
        return salt
    }

    private fun generateRandomSalt(): String {
        val secureRandom = SecureRandom()
        val saltBytes = ByteArray(32)
        secureRandom.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }
}
