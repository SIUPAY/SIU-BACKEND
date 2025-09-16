package siu.siubackend.user.application.service

import org.springframework.stereotype.Service
import siu.siubackend.user.application.port.input.GetUserUseCase
import siu.siubackend.user.application.port.output.UserRepository
import siu.siubackend.user.domain.User
import java.util.UUID

@Service
class GetUserService(
    private val userRepository: UserRepository
) : GetUserUseCase {

    override fun getUser(userIdentifier: UUID): User {
        return userRepository.findByIdentifier(userIdentifier)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
    }
}
