package siu.siubackend.store.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import siu.siubackend.common.image.ImageUploadService
import siu.siubackend.store.application.port.input.CreateStoreRequest
import siu.siubackend.store.application.port.input.CreateStoreResponse
import siu.siubackend.store.application.port.input.CreateStoreUseCase
import siu.siubackend.store.application.port.output.StoreRepository
import siu.siubackend.store.domain.service.StoreFactory
import siu.siubackend.user.application.port.output.UserRepository
import java.util.*

@Service
@Transactional
class CreateStoreService(
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository,
    private val imageUploadService: ImageUploadService
) : CreateStoreUseCase {

    override fun createStore(userIdentifier: UUID, request: CreateStoreRequest, imageFile: MultipartFile): CreateStoreResponse {
        val user = userRepository.findByIdentifier(userIdentifier)
            ?: throw IllegalArgumentException("User not found with identifier: $userIdentifier")

        val imageUploadResult = imageUploadService.uploadImage(imageFile)
        if (!imageUploadResult.success || imageUploadResult.url == null) {
            throw IllegalStateException("Failed to upload image: ${imageUploadResult.message}")
        }

        val store = StoreFactory.create(
            name = request.name,
            address = request.address,
            phone = request.phone,
            profileImgUrl = imageUploadResult.url,
            walletAddress = request.walletAddress,
            location = request.location
        )

        val savedStore = storeRepository.save(store)

        val updatedUser = user.updateStoreIdentifier(savedStore.identifier)
        userRepository.save(updatedUser)

        return CreateStoreResponse(identifier = savedStore.identifier)
    }
}
