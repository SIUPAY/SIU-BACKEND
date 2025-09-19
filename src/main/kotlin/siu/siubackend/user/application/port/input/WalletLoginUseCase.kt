package siu.siubackend.user.application.port.input

import java.util.*

interface WalletLoginUseCase {
    data class Command(
        val walletAddress: String
    )
    
    data class Result(
        val identifier: UUID,
        val walletAddress: String,
        val storeIdentifier: UUID?
    )
    
    fun login(command: Command): Result
}
