package siu.siubackend.menu.adapter.out.external

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import siu.siubackend.common.image.ExternalImageUploadService
import siu.siubackend.common.image.ImageUploadResult
import siu.siubackend.menu.application.port.output.ImageUploader
import java.io.ByteArrayInputStream
import java.io.InputStream

@Component
class ImageUploaderImpl(
    private val external: ExternalImageUploadService
) : ImageUploader {

    override fun upload(bytes: ByteArray, originalName: String?): String {
        val mf = object : MultipartFile {
            override fun getName(): String = "image"
            override fun getOriginalFilename(): String? = originalName ?: "menu.jpg"
            override fun getContentType(): String? = null // 필요하면 "image/jpeg" 등 지정
            override fun isEmpty(): Boolean = bytes.isEmpty()
            override fun getSize(): Long = bytes.size.toLong()
            override fun getBytes(): ByteArray = bytes
            override fun getInputStream(): InputStream = ByteArrayInputStream(bytes)
            override fun transferTo(dest: java.io.File) {
                dest.writeBytes(bytes)
            }
        }

        val res: ImageUploadResult = external.uploadImage(mf)
        require(res.success && !res.url.isNullOrBlank()) {
            "이미지 업로드 실패: ${res.message}"
        }
        return res.url!!
    }
}