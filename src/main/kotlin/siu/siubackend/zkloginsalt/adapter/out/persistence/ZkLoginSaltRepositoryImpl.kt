package siu.siubackend.zkloginsalt.adapter.out.persistence

import org.springframework.stereotype.Repository
import siu.siubackend.zkloginsalt.application.port.out.ZkLoginSaltRepository
import siu.siubackend.zkloginsalt.domain.ZkLoginSalt

@Repository
class ZkLoginSaltRepositoryImpl(
    private val zkLoginSaltJpaRepository: ZkLoginSaltJpaRepository
) : ZkLoginSaltRepository {

    override fun findByOauthUserId(oauthUserId: String): ZkLoginSalt? {
        return zkLoginSaltJpaRepository.findByOauthUserId(oauthUserId)?.toDomain()
    }

    override fun save(zkLoginSalt: ZkLoginSalt): ZkLoginSalt {
        val entity = zkLoginSalt.toEntity()
        val savedEntity = zkLoginSaltJpaRepository.save(entity)
        return savedEntity.toDomain()
    }
}

fun ZkLoginSaltEntity.toDomain(): ZkLoginSalt {
    return ZkLoginSalt(
        identifier = this.identifier,
        oauthUserId = this.oauthUserId,
        zkloginSalt = this.zkloginSalt
    )
}

fun ZkLoginSalt.toEntity(): ZkLoginSaltEntity {
    return ZkLoginSaltEntity(
        identifier = this.identifier,
        oauthUserId = this.oauthUserId,
        zkloginSalt = this.zkloginSalt
    )
}
