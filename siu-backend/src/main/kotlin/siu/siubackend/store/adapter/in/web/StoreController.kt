package siu.siubackend.store.adapter.`in`.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import siu.siubackend.store.adapter.`in`.web.dto.CreateStoreResponseDto
import siu.siubackend.store.adapter.`in`.web.dto.StoreDataDto
import siu.siubackend.store.application.port.input.CreateStoreRequest
import siu.siubackend.store.application.port.input.CreateStoreUseCase

@RestController
@RequestMapping("/api/stores")
class StoreController(
    private val createStoreUseCase: CreateStoreUseCase
) {

    @PostMapping
    fun createStore(
        @RequestPart("data") storeData: StoreDataDto,
        @RequestPart("image") imageFile: MultipartFile
    ): ResponseEntity<CreateStoreResponseDto> {
        val request = CreateStoreRequest(
            name = storeData.name,
            address = storeData.address,
            phone = storeData.phone,
            walletAddress = storeData.walletAddress
        )

        val response = createStoreUseCase.createStore(storeData.userIdentifier, request, imageFile)
        
        return ResponseEntity.ok(CreateStoreResponseDto(identifier = response.identifier))
    }
}
