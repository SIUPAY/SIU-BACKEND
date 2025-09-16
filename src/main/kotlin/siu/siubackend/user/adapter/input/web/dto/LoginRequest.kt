package siu.siubackend.user.adapter.input.web.dto

enum class LoginType {
    ZKLOGIN, WALLET
}

data class LoginRequest(
    val loginType: LoginType,
    val walletAddress: String? = null,
    val oauthUserId: String? = null
)
