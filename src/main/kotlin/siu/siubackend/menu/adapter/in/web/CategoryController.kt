package siu.siubackend.menu.adapter.`in`.web

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.menu.adapter.`in`.dto.CreateCategoryRequest
import siu.siubackend.menu.application.port.input.CreateCategoryUseCase
import java.time.OffsetDateTime
import java.util.*

@RestController
class CategoryController(
    private val createCategory: CreateCategoryUseCase
) {
    @PostMapping("/api/stores/{store_identifier}/menus/category")
    fun create(
        @PathVariable("store_identifier") storeIdentifier: UUID,
        @Valid @RequestBody req: CreateCategoryRequest
    ): ResponseEntity<Map<String, Any?>> {
        val id = createCategory.handle(
            CreateCategoryUseCase.Command(
                storeIdentifier = storeIdentifier,
                name = req.name,
                description = req.description,
                displayOrder = req.display_order
            )
        )
        val now = OffsetDateTime.now()
        return ResponseEntity.status(201).body(
            mapOf(
                "category_identifier" to id,
                "store_identifier" to storeIdentifier,
                "name" to req.name,
                "description" to req.description,
                "display_order" to req.display_order,
                "created_at" to now
            )
        )
    }
}


