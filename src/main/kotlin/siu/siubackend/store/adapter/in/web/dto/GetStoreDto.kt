package siu.siubackend.store.adapter.`in`.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime
import java.util.*

data class GetStoreResponseDto(
    val identifier: UUID,
    val name: String,
    val address: String,
    val phone: String?,
    @JsonProperty("profile_img_url")
    val profileImgUrl: String,
    @JsonProperty("wallet_address")
    val walletAddress: String,
    @JsonProperty("created_date")
    val createdDate: OffsetDateTime
)
