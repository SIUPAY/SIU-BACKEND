package siu.siubackend.user.adapter.output.persistence

import org.springframework.stereotype.Repository
import siu.siubackend.user.adapter.output.persistence.UserEntity.Companion.toEntity
import siu.siubackend.user.application.port.output.UserRepository
import siu.siubackend.user.domain.User
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun findByOauthUserId(oauthUserId: String): User? {
        return userJpaRepository.findByOauthUserId(oauthUserId)?.toDomain()
    }

    override fun findByIdentifier(identifier: UUID): User? {
        return userJpaRepository.findById(identifier).orElse(null)?.toDomain()
    }

    override fun save(user: User): User {
        val userEntity = user.toEntity()
        val savedEntity = userJpaRepository.save(userEntity)
        return savedEntity.toDomain()
    }
}
