package siu.siubackend.user.adapter.input.web

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import siu.siubackend.user.adapter.input.web.dto.UpdateProfileRequest
import siu.siubackend.user.adapter.input.web.dto.UserResponse
import siu.siubackend.user.application.port.input.GetUserUseCase
import siu.siubackend.user.application.port.input.UpdateProfileUseCase
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getUserUseCase: GetUserUseCase
) {

    @GetMapping("/{user_identifier}")
    fun getUser(
        @PathVariable("user_identifier") userIdentifier: UUID
    ): ResponseEntity<UserResponse> {
        val user = getUserUseCase.getUser(userIdentifier)

        val response = UserResponse(
            identifier = user.identifier,
            nickname = user.nickname,
            profileImgUrl = user.profileImgUrl,
            storeIdentifier = user.storeIdentifier
        )
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "프로필 수정",
        description = "multipart/form-data로 JSON(data) + 이미지(image) 업로드"
    )
    @PutMapping(
        "/{user_identifier}/profile",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun updateProfile(
        @PathVariable("user_identifier") userIdentifier: UUID,
        @Parameter(
            name = "data",
            required = true,
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UpdateProfileRequest::class))]
        )
        @Valid @RequestPart("data") updateProfileRequest: UpdateProfileRequest,
        @Parameter(
            name = "image",
            required = true,
            content = [Content(mediaType = "image/*", schema = Schema(type = "string", format = "binary"))]
        )
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
