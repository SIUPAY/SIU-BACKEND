package siu.siubackend.menu.application.port.output

interface ImageUploader {
    fun upload(bytes: ByteArray, originalName: String?): String
}