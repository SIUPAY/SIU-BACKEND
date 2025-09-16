package siu.siubackend.store.adapter.`in`.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime
import java.util.*

data class SearchStoreResponseDto(
    val identifier: UUID,
    val name: String,
    @JsonProperty("profile_img_url")
    val profileImgUrl: String,
    @JsonProperty("wallet_address")
    val walletAddress: String,
    val distance: Double,
    @JsonProperty("total_order_count")
    val totalOrderCount: Int,
    @JsonProperty("created_date")
    val createdDate: OffsetDateTime
)
