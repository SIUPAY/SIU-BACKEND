package siu.siubackend.zkloginsalt.adapter.out.persistence

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "zklogin_salts")
class ZkLoginSaltEntity(
    @Id
    @Column(name = "identifier", columnDefinition = "UUID")
    val identifier: UUID = UUID.randomUUID(),
    
    @Column(name = "oauth_user_id", nullable = false, unique = true)
    val oauthUserId: String,
    
    @Column(name = "zklogin_salt", nullable = false)
    val zkloginSalt: String
)
