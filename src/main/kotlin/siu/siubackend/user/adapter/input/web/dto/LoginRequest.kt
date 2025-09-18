package siu.siubackend.user.adapter.input.web.dto

enum class LoginType {
    ZKLOGIN, WALLET
}

data class LoginRequest(
    val login_type: LoginType,
    val wallet_address: String,
    val oauth_user_id: String? = null
)
