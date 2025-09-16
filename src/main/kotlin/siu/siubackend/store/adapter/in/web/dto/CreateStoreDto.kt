package siu.siubackend.store.adapter.`in`.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class StoreDataDto(
    @JsonProperty("user_identifier")
    val userIdentifier: UUID,
    val name: String,
    val address: String,
    val phone: String?,
    @JsonProperty("wallet_address")
    val walletAddress: String,
    val latitude: Double,
    val longitude: Double
)

data class CreateStoreResponseDto(
    val identifier: UUID
)
