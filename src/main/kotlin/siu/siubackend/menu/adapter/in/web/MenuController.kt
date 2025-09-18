package siu.siubackend.menu.adapter.`in`.web

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import siu.siubackend.menu.adapter.`in`.dto.CreateMenuPayload
import siu.siubackend.menu.adapter.`in`.dto.MenuResponse
import siu.siubackend.menu.adapter.`in`.dto.UpdateMenuRequest
import siu.siubackend.menu.application.port.input.*
import siu.siubackend.menu.application.port.output.CategoryRepository
import siu.siubackend.menu.domain.Menu
import java.util.*

@RestController
@RequestMapping
class MenuController(
    private val createMenu: CreateMenuUseCase,
    private val updateMenu: UpdateMenuUseCase,
    private val deleteMenu: DeleteMenuUseCase,
    private val listMenus: ListMenusUseCase,
    private val listMenusByStore: ListMenusByStoreUseCase,
    private val objectMapper: ObjectMapper,
    private val categoryRepository: CategoryRepository
) {
    // GET /api/menus
    @GetMapping("/api/menus")
    fun getMenus(): ResponseEntity<Map<String, List<MenuResponse>>> {
        val menus = listMenus.handle().map { it.toResponse() }
        return ResponseEntity.ok(mapOf("menus" to menus))
    }

    // POST /api/stores/{store_identifier}/menu (multipart) -> 201
    @Operation(
        summary = "메뉴 생성",
        description = "multipart/form-data로 JSON(menu) + 이미지(image) 업로드"
    )
    @PostMapping(
        "/api/stores/{store_identifier}/menu",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun create(
        @PathVariable("store_identifier") storeIdentifier: UUID,
        @Parameter(
            name = "menu",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = CreateMenuPayload::class),
                examples = [ExampleObject(value = """{\n  \"category_identifier\": \"<uuid>\",\n  \"name\": \"아메리카노\",\n  \"price\": 4500,\n  \"description\": \"hot\",\n  \"is_available\": true\n}""")]
            )]
        )
        @RequestPart("menu") menuJson: String,
        @Parameter(name = "image", content = [Content(mediaType = "image/*", schema = Schema(type = "string", format = "binary"))])
        @RequestPart("image", required = false) image: MultipartFile?
    ): ResponseEntity<Map<String, Any?>> {
        val payload = objectMapper.readValue(menuJson, CreateMenuPayload::class.java)
        val saved = createMenu.handle(
            CreateMenuUseCase.Command(
                storeIdentifier = storeIdentifier,
                categoryIdentifier = payload.category_identifier,
                name = payload.name,
                price = payload.price,
                description = payload.description,
                isAvailable = payload.is_available,
                imageFileBytes = image?.bytes,
                imageOriginalName = image?.originalFilename
            )
        )
        val body = mapOf(
            "menu_identifier" to saved.identifier,
            "store_identifier" to saved.storeIdentifier,
            "category_identifier" to saved.categoryIdentifier,
            "name" to saved.name,
            "description" to saved.description,
            "price" to saved.price,
            "currency" to "KRW",
            "is_available" to saved.isAvailable,
            "image_url" to saved.imageUrl,
            "created_at" to saved.createdDate
        )
        return ResponseEntity.status(201).body(body)
    }

    // PUT /api/menus/{id} (menu JSON + image File?) -> 204
    @Operation(
        summary = "메뉴 수정",
        description = "multipart/form-data로 JSON(menu) + 이미지(image) 업로드"
    )
    @PutMapping(
        "/api/menus/{id}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun update(
        @PathVariable id: UUID,
        @Parameter(
            name = "menu",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = UpdateMenuRequest::class),
                examples = [ExampleObject(value = """{\n  \"name\": \"라떼\",\n  \"price\": 5000,\n  \"description\": \"iced\",\n  \"category_identifier\": null\n}""")]
            )]
        )
        @RequestPart("menu") menuJson: String,
        @Parameter(name = "image", content = [Content(mediaType = "image/*", schema = Schema(type = "string", format = "binary"))])
        @RequestPart("image", required = false) image: MultipartFile?
    ): ResponseEntity<Void> {
        val req = objectMapper.readValue(menuJson, UpdateMenuRequest::class.java)
        updateMenu.handle(
            id,
            UpdateMenuUseCase.Command(
                name = req.name,
                price = req.price,
                description = req.description,
                categoryIdentifier = req.category_identifier,
                isAvailable = req.is_available,
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
    fun getByStore(
        @PathVariable("store_identifier") storeId: UUID,
        @RequestParam("category_id", required = false) categoryId: UUID?,
        @RequestParam("available", required = false) available: Boolean?
    ): ResponseEntity<Map<String, Any?>> {
        val menus = listMenusByStore.handle(storeId, categoryId, available)

        // 카테고리별 그룹으로 응답 변환
        val grouped = menus.groupBy { it.categoryIdentifier }
            .entries
            .sortedBy { it.key?.toString() }
            .map { (catId, list) ->
                val meta = catId?.let { cid ->
                    categoryRepository.findById(cid)
                }
                mapOf(
                    "category_identifier" to catId,
                    "name" to meta?.name,
                    "description" to meta?.description,
                    "display_order" to meta?.displayOrder,
                    "menus" to list.map { m ->
                        mapOf(
                            "menu_identifier" to m.identifier,
                            "name" to m.name,
                            "description" to m.description,
                            "price" to m.price,
                            "currency" to "KRW",
                            "is_available" to m.isAvailable,
                            "image_url" to m.imageUrl,
                            "created_at" to m.createdDate
                        )
                    }
                )
            }

        val body = mapOf(
            "store_identifier" to storeId,
            "categories" to grouped
        )
        return ResponseEntity.ok(body)
    }
}

private fun Menu.toResponse() = MenuResponse(
    identifier = this.identifier,
    name = this.name,
    price = this.price,
    image_url = this.imageUrl,
    is_available = this.isAvailable,
    created_date = this.createdDate
)