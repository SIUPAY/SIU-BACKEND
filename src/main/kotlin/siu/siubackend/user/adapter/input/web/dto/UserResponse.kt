package siu.siubackend.user.adapter.input.web.dto

import java.util.UUID

data class UserResponse(
    val identifier: UUID,
    val nickname: String,
    val profileImgUrl: String,
    val storeIdentifier: UUID?
)
