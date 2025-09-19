package siu.siubackend.zkloginsalt.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ZkLoginSaltJpaRepository : JpaRepository<ZkLoginSaltEntity, UUID> {
    fun findByOauthUserId(oauthUserId: String): ZkLoginSaltEntity?
}
