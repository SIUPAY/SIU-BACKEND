package siu.siubackend.menu.adapter.`in`.web

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import siu.siubackend.menu.adapter.`in`.dto.CreateCategoryRequest
import siu.siubackend.menu.application.port.input.CreateCategoryUseCase
import java.util.*

@RestController
class CategoryController(
    private val createCategory: CreateCategoryUseCase
) {
    @PostMapping("/api/categories")
    fun create(@Valid @RequestBody req: CreateCategoryRequest): ResponseEntity<Void> {
        val id = createCategory.handle(CreateCategoryUseCase.Command(name = req.name))
        return ResponseEntity.noContent()
            .header("Location", "/api/categories/$id")
            .build()
    }
}


