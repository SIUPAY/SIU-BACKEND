package siu.siubackend.zkloginsalt.application.port.`in`

interface GenerateZkLoginSaltUseCase {
    data class Command(
        val oauthUserId: String
    )
    
    fun generateSalt(command: Command): String
}
