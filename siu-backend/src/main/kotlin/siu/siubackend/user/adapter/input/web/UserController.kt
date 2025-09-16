package siu.siubackend.user.adapter.input.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import siu.siubackend.user.adapter.input.web.dto.UpdateProfileRequest
import siu.siubackend.user.application.port.input.UpdateProfileUseCase
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController(
    private val updateProfileUseCase: UpdateProfileUseCase
) {

    @PutMapping("/{user_identifier}/profile")
    fun updateProfile(
        @PathVariable("user_identifier") userIdentifier: UUID,
        @RequestPart("data") updateProfileRequest: UpdateProfileRequest,
        @RequestPart("image") image: MultipartFile
    ): ResponseEntity<Void> {
        updateProfileUseCase.updateProfile(
            userIdentifier = userIdentifier,
            nickname = updateProfileRequest.nickname,
            profileImage = image
        )
        return ResponseEntity.noContent().build()
    }
}
