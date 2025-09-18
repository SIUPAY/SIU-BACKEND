package siu.siubackend.user.adapter.input.web.dto

import java.util.UUID

data class LoginResponse(
    val identifier: UUID,
    val wallet_address: String,
    val zklogin_salt: String? = null,
    val store_identifier: UUID? = null
)
