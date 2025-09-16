package siu.siubackend.user.application.port.output

import org.springframework.web.multipart.MultipartFile

data class ImageUploadResult(
    val success: Boolean,
    val message: String,
    val url: String?,
    val fileId: Int?
)

interface ImageUploadService {
    fun uploadImage(imageFile: MultipartFile): ImageUploadResult
}
