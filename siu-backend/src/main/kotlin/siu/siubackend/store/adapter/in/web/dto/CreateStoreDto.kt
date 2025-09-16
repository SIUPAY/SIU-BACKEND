package siu.siubackend.store.adapter.`in`.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class StoreDataDto(
    val userIdentifier: UUID,
    val name: String,
    val address: String,
    val phone: String?,
    val walletAddress: String
)

data class CreateStoreResponseDto(
    val identifier: UUID
)
