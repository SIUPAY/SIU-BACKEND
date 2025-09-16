package siu.siubackend.store.application.port.input

import org.springframework.web.multipart.MultipartFile
import java.util.*

data class CreateStoreRequest(
    val name: String,
    val address: String,
    val phone: String?,
    val walletAddress: String
)

data class CreateStoreResponse(
    val identifier: UUID
)

interface CreateStoreUseCase {
    fun createStore(userIdentifier: UUID, request: CreateStoreRequest, imageFile: MultipartFile): CreateStoreResponse
}
