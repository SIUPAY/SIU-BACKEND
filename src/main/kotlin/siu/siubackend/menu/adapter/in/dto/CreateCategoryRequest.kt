package siu.siubackend.menu.adapter.`in`.dto

import jakarta.validation.constraints.NotBlank

data class CreateCategoryRequest(
    @field:NotBlank val name: String
)


