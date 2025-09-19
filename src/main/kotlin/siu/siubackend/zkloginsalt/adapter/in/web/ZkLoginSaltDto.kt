package siu.siubackend.zkloginsalt.adapter.`in`.web

data class ZkLoginSaltRequest(
    val oauthUserId: String
)

data class ZkLoginSaltResponse(
    val zkloginSalt: String
)
