package siu.siubackend.user.domain

import java.time.OffsetDateTime
import java.util.UUID

data class User(
    val identifier: UUID,
    val nickname: String,
    val profileImgUrl: String,
    val oauthUserId: String?,
    val zkloginSalt: String?,
    val walletAddress: String?,
    val storeIdentifier: UUID?,
    val createdDate: OffsetDateTime
) {

    fun updateProfile(nickname: String, profileImgUrl: String): User {
        return this.copy(
            nickname = nickname,
            profileImgUrl = profileImgUrl
        )
    }

    fun updateStoreIdentifier(storeIdentifier: UUID): User {
        return this.copy(
            storeIdentifier = storeIdentifier
        )
    }

    fun updateWalletAddress(walletAddress: String): User {
        return this.copy(
            walletAddress = walletAddress
        )
    }
}
