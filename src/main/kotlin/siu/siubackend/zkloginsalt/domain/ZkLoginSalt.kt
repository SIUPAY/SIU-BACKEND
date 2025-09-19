package siu.siubackend.zkloginsalt.domain

import java.util.*

data class ZkLoginSalt(
    val identifier: UUID,
    val oauthUserId: String,
    val zkloginSalt: String
)
