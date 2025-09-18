package siu.siubackend.menu.adapter.`in`.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.util.*

data class UpdateMenuRequest(
    @field:NotBlank val name: String,
    @field:Min(0) val price: Int,
    val description: String?,
    val category_identifier: UUID?,
    val is_available: Boolean?
)