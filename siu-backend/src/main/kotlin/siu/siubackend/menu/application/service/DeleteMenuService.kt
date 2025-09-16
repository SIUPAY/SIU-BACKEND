package siu.siubackend.menu.application.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import siu.siubackend.menu.application.port.input.DeleteMenuUseCase
import siu.siubackend.menu.application.port.output.MenuRepository
import java.util.*

@Service
class DeleteMenuService(
    private val repo: MenuRepository
) : DeleteMenuUseCase {

    @Transactional
    override fun handle(id: UUID) {
        repo.deleteById(id)
    }
}