package siu.siubackend.user.domain.service

import siu.siubackend.user.domain.User
import java.security.SecureRandom
import java.time.OffsetDateTime
import java.util.*

object UserFactory {
    
    fun createUserForZkLogin(oauthUserId: String): User {
        return User(
            identifier = UUID.randomUUID(),
            nickname = generateRandomNickname(),
            profileImgUrl = generateDefaultProfileImageUrl(),
            oauthUserId = oauthUserId,
            zkloginSalt = generateZkLoginSalt(),
            walletAddress = null,
            storeIdentifier = null,
            createdDate = OffsetDateTime.now(),
        )
    }

    private fun generateRandomNickname(): String {
        val adjectives = listOf("Happy", "Lucky", "Bright", "Swift", "Cool", "Smart", "Kind", "Bold")
        val nouns = listOf("Tiger", "Eagle", "Lion", "Wolf", "Bear", "Fox", "Hawk", "Dragon")
        val randomNumber = (1000..9999).random()
        
        return "${adjectives.random()}${nouns.random()}$randomNumber"
    }

    private fun generateDefaultProfileImageUrl(): String {
        val avatarId = (1..100).random()
        return "https://api.dicebear.com/7.x/avataaars/svg?seed=$avatarId"
    }

    private fun generateZkLoginSalt(): String {
        val secureRandom = SecureRandom()
        val saltBytes = ByteArray(32)
        secureRandom.nextBytes(saltBytes)
        return Base64.getEncoder().encodeToString(saltBytes)
    }
}
