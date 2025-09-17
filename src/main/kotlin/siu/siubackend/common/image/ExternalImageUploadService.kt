package siu.siubackend.common.image

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile

@Component
class ExternalImageUploadService(
    private val restTemplate: RestTemplate
) : ImageUploadService {

    private val imageUploadUrl = "https://img.allora.pw/store-image"

    override fun uploadImage(imageFile: MultipartFile): ImageUploadResult {
        return try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.MULTIPART_FORM_DATA

            val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
            
            val resource = object : ByteArrayResource(imageFile.bytes) {
                override fun getFilename(): String? {
                    return imageFile.originalFilename
                }
            }
            
            body.add("image", resource)
            if (imageFile.originalFilename != null) {
                body.add("original_name", imageFile.originalFilename)
            }

            val requestEntity = HttpEntity(body, headers)
            val response = restTemplate.postForObject(imageUploadUrl, requestEntity, Map::class.java)

            if (response != null && response["success"] == true) {
                ImageUploadResult(
                    success = true,
                    message = response["message"] as String,
                    url = response["url"] as String,
                    fileId = response["file_id"] as Int?
                )
            } else {
                ImageUploadResult(
                    success = false,
                    message = response?.get("message") as String? ?: "알 수 없는 오류",
                    url = null,
                    fileId = null
                )
            }
        } catch (e: Exception) {
            ImageUploadResult(
                success = false,
                message = "이미지 업로드 실패: ${e.message}",
                url = null,
                fileId = null
            )
        }
    }
}
