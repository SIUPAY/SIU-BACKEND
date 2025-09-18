package siu.siubackend.menu.adapter.`in`.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class CreateMenuPayload(
    val category_identifier: UUID?,
    @field:NotBlank val name: String,
    @field:Min(0) val price: Int,
    val description: String?,
    val is_available: Boolean?
)