package siu.siubackend.user.adapter.input.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile

@Schema(name = "UpdateProfileMultipart")
data class UpdateProfileMultipart(
    @field:Schema(description = "프로필 데이터(JSON)")
    val data: UpdateProfileRequest,

    @field:Schema(type = "string", format = "binary", description = "프로필 이미지 파일")
    val image: MultipartFile
)


