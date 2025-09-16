package siu.siubackend.user.domain

import siu.siubackend.user.adapter.output.persistence.UserEntity
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
)
