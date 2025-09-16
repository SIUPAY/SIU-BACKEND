package siu.siubackend.user.adapter.input.web.dto

import java.util.UUID

data class ZkLoginResponse(
    val identifier: UUID,
    val zkloginSalt: String
)
