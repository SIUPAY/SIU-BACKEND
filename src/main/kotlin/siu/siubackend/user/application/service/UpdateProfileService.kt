package siu.siubackend.user.application.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import siu.siubackend.common.image.ImageUploadService
import siu.siubackend.user.application.port.input.UpdateProfileUseCase
import siu.siubackend.user.application.port.output.UserRepository
import java.util.UUID

@Service
class UpdateProfileService(
    private val userRepository: UserRepository,
    private val imageUploadService: ImageUploadService
) : UpdateProfileUseCase {

    override fun updateProfile(userIdentifier: UUID, nickname: String, profileImage: MultipartFile) {
        val user = userRepository.findByIdentifier(userIdentifier)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val imageUploadResult = imageUploadService.uploadImage(profileImage)
        if (!imageUploadResult.success || imageUploadResult.url == null) {
            throw RuntimeException("이미지 업로드에 실패했습니다: ${imageUploadResult.message}")
        }

        val updatedUser = user.updateProfile(nickname, imageUploadResult.url)

        userRepository.save(updatedUser)
    }
}
