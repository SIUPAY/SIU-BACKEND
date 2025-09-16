package siu.siubackend.store.adapter.`in`.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import siu.siubackend.store.adapter.`in`.web.dto.CreateStoreResponseDto
import siu.siubackend.store.adapter.`in`.web.dto.GetStoreResponseDto
import siu.siubackend.store.adapter.`in`.web.dto.SearchStoreResponseDto
import siu.siubackend.store.adapter.`in`.web.dto.StoreDataDto
import siu.siubackend.store.application.port.input.CreateStoreRequest
import siu.siubackend.store.application.port.input.CreateStoreUseCase
import siu.siubackend.store.application.port.input.GetStoreUseCase
import siu.siubackend.store.application.port.input.SearchStoreRequest
import siu.siubackend.store.application.port.input.SearchStoreUseCase
import siu.siubackend.store.domain.Location
import java.util.*

@RestController
@RequestMapping("/api/stores")
class StoreController(
    private val createStoreUseCase: CreateStoreUseCase,
    private val searchStoreUseCase: SearchStoreUseCase,
    private val getStoreUseCase: GetStoreUseCase
) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun createStore(
        @RequestPart("data") storeData: StoreDataDto,
        @RequestPart("image") imageFile: MultipartFile
    ): ResponseEntity<CreateStoreResponseDto> {
        val request = CreateStoreRequest(
            name = storeData.name,
            address = storeData.address,
            phone = storeData.phone,
            walletAddress = storeData.walletAddress,
            location = Location(
                latitude = storeData.latitude,
                longitude = storeData.longitude
            )
        )

        val response = createStoreUseCase.createStore(storeData.userIdentifier, request, imageFile)
        
        return ResponseEntity.ok(CreateStoreResponseDto(identifier = response.identifier))
    }

    @GetMapping
    fun searchStores(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam distance: Int,
        @RequestParam(required = false) query: String?
    ): ResponseEntity<List<SearchStoreResponseDto>> {
        val searchRequest = SearchStoreRequest(
            location = Location(latitude = latitude, longitude = longitude),
            distance = distance,
            query = query
        )

        val results = searchStoreUseCase.searchStores(searchRequest)
        
        val response = results.map { result ->
            SearchStoreResponseDto(
                identifier = result.store.identifier,
                name = result.store.name,
                profileImgUrl = result.store.profileImgUrl,
                walletAddress = result.store.walletAddress,
                distance = result.distance,
                totalOrderCount = result.store.totalOrderCount,
                createdDate = result.store.createdDate
            )
        }

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{storeIdentifier}")
    fun getStore(
        @PathVariable storeIdentifier: UUID
    ): ResponseEntity<GetStoreResponseDto> {
        val store = getStoreUseCase.getStore(storeIdentifier)
            ?: return ResponseEntity.notFound().build()

        val response = GetStoreResponseDto(
            identifier = store.identifier,
            name = store.name,
            address = store.address,
            phone = store.phone,
            profileImgUrl = store.profileImgUrl,
            walletAddress = store.walletAddress,
            createdDate = store.createdDate
        )

        return ResponseEntity.ok(response)
    }
}
