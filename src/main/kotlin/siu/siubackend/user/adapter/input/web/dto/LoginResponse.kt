package siu.siubackend.user.adapter.input.web.dto

import java.util.UUID

data class LoginResponse(
    val identifier: UUID,
    val zkloginSalt: String? = null,
    val walletAddress: String? = null,
    val storeIdentifier: UUID? = null
)
