package siu.siubackend.menu.adapter.`in`.web

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import siu.siubackend.menu.adapter.`in`.dto.CreateMenuPayload
import siu.siubackend.menu.adapter.`in`.dto.MenuResponse
import siu.siubackend.menu.adapter.`in`.dto.UpdateMenuRequest
import siu.siubackend.menu.application.port.input.*
import siu.siubackend.menu.domain.Menu
import java.util.*

@RestController
@RequestMapping
class MenuController(
    private val createMenu: CreateMenuUseCase,
    private val updateMenu: UpdateMenuUseCase,
    private val deleteMenu: DeleteMenuUseCase,
    private val listMenus: ListMenusUseCase,
    private val listMenusByStore: ListMenusByStoreUseCase
) {
    // GET /api/menus
    @GetMapping("/api/menus")
    fun getMenus(): ResponseEntity<Map<String, List<MenuResponse>>> {
        val menus = listMenus.handle().map { it.toResponse() }
        return ResponseEntity.ok(mapOf("menus" to menus))
    }

    // POST /api/menus (menu JSON + image File) -> 204
    @PostMapping(
        "/api/menus",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun create(
        @Valid @RequestPart("menu") payload: CreateMenuPayload,
        @RequestPart("image", required = false) image: MultipartFile?
    ): ResponseEntity<Void> {
        createMenu.handle(
            CreateMenuUseCase.Command(
                storeIdentifier = payload.store_identifier,
                categoryIdentifier = payload.category_identifier,
                name = payload.name,
                price = payload.price,
                description = payload.description,
                imageFileBytes = image?.bytes,
                imageOriginalName = image?.originalFilename
            )
        )
        return ResponseEntity.noContent().build()
    }

    // PUT /api/menus/{id} (menu JSON + image File?) -> 204
    @PutMapping(
        "/api/menus/{id}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestPart("menu") req: UpdateMenuRequest,
        @RequestPart("image", required = false) image: MultipartFile?
    ): ResponseEntity<Void> {
        updateMenu.handle(
            id,
            UpdateMenuUseCase.Command(
                name = req.name,
                price = req.price,
                description = req.description,
                categoryIdentifier = req.category_identifier,
                imageFileBytes = image?.bytes,
                imageOriginalName = image?.originalFilename
            )
        )
        return ResponseEntity.noContent().build()
    }

    // DELETE /api/menus/{id} -> 204
    @DeleteMapping("/api/menus/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        deleteMenu.handle(id)
        return ResponseEntity.noContent().build()
    }

    // GET /api/stores/{store_identifier}/menus
    @GetMapping("/api/stores/{store_identifier}/menus")
    fun getByStore(@PathVariable("store_identifier") storeId: UUID)
            : ResponseEntity<Map<String, List<MenuResponse>>> {
        val menus = listMenusByStore.handle(storeId).map { it.toResponse() }
        return ResponseEntity.ok(mapOf("menus" to menus))
    }
}

private fun Menu.toResponse() = MenuResponse(
    identifier = this.identifier,
    name = this.name,
    price = this.price,
    image_url = this.imageUrl,
    created_date = this.createdDate
)