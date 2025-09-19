package siu.siubackend.user.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import siu.siubackend.user.application.port.input.WalletLoginUseCase
import siu.siubackend.user.application.port.output.UserRepository
import siu.siubackend.user.domain.service.UserFactory
import java.time.OffsetDateTime

@Service
@Transactional
class WalletLoginService(
    private val userRepository: UserRepository
) : WalletLoginUseCase {

    override fun login(command: WalletLoginUseCase.Command): WalletLoginUseCase.Result {
        val existingUser = userRepository.findByWalletAddress(command.walletAddress)
        
        if (existingUser != null) {
            return WalletLoginUseCase.Result(
                identifier = existingUser.identifier,
                walletAddress = existingUser.walletAddress!!,
                storeIdentifier = existingUser.storeIdentifier
            )
        }
        
        val newUser = UserFactory.createWithWallet(
            walletAddress = command.walletAddress,
            createdDate = OffsetDateTime.now()
        )
        
        val savedUser = userRepository.save(newUser)
        
        return WalletLoginUseCase.Result(
            identifier = savedUser.identifier,
            walletAddress = savedUser.walletAddress!!,
            storeIdentifier = savedUser.storeIdentifier
        )
    }
}
