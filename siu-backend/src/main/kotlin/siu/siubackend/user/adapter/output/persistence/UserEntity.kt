package siu.siubackend.user.adapter.output.persistence

import jakarta.persistence.*
import siu.siubackend.user.domain.User
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "user")
class UserEntity(

    @Id
    @Column(name = "identifier", nullable = false)
    val identifier: UUID,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Column(name = "profile_img_url", nullable = false)
    var profileImgUrl: String,

    @Column(name = "oauth_user_id")
    var oauthUserId: String?,

    @Column(name = "zklogin_salt")
    var zkloginSalt: String?,

    @Column(name = "wallet_address")
    var walletAddress: String?,

    @Column(name = "store_identifier")
    var storeIdentifier: UUID?,

    @Column(name = "created_date", nullable = false)
    var createdDate: OffsetDateTime
) {

    fun toDomain(): User {
        return User(
            identifier = this.identifier,
            nickname = this.nickname,
            profileImgUrl = this.profileImgUrl,
            oauthUserId = this.oauthUserId,
            zkloginSalt = this.zkloginSalt,
            walletAddress = this.walletAddress,
            storeIdentifier = this.storeIdentifier,
            createdDate = this.createdDate
        )
    }

    companion object {
        fun User.toEntity(user: User): UserEntity {
            return UserEntity(
                identifier = user.identifier,
                nickname = user.nickname,
                profileImgUrl = user.profileImgUrl,
                oauthUserId = user.oauthUserId,
                zkloginSalt = user.zkloginSalt,
                walletAddress = user.walletAddress,
                storeIdentifier = user.storeIdentifier,
                createdDate = user.createdDate
            )
        }
    }
}
