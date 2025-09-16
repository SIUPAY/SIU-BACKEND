package siu.siubackend.user.application.port.input

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface UpdateProfileUseCase {
    fun updateProfile(userIdentifier: UUID, nickname: String, profileImage: MultipartFile)
}
