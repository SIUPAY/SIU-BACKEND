package siu.siubackend.user.adapter.input.web.dto

import java.util.*

data class WalletLoginRequest(
    val walletAddress: String
)

data class WalletLoginResponse(
    val identifier: UUID,
    val walletAddress: String,
    val storeIdentifier: UUID?
)
