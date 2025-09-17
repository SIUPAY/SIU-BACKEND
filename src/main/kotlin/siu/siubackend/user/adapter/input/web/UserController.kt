package siu.siubackend.user.adapter.input.web

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Encoding
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
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
        description = "multipart/form-data로 JSON(data) + 이미지(image) 업로드",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "multipart/form-data",
                    encoding = [
                        Encoding(name = "data", contentType = "application/json"),
                        Encoding(name = "image", contentType = "image/*")
                    ],
                    schema = Schema(
                        type = "object"
                    )
                )
            ]
        )
    )
    @PutMapping(
        "/{user_identifier}/profile",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun updateProfile(
        @PathVariable("user_identifier") userIdentifier: UUID,
        @Valid @RequestPart("data") updateProfileRequest: UpdateProfileRequest,
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
