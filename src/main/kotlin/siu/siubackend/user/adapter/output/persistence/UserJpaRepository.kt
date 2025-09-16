package siu.siubackend.user.adapter.output.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<UserEntity, UUID> {
    fun findByOauthUserId(oauthUserId: String): UserEntity?
    fun findByWalletAddress(walletAddress: String): UserEntity?
}
