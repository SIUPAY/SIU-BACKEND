package siu.siubackend.menu.application.service

import org.springframework.stereotype.Service
import siu.siubackend.menu.application.port.input.ListMenusByStoreUseCase
import siu.siubackend.menu.application.port.output.MenuRepository
import siu.siubackend.menu.domain.Menu
import java.util.*


@Service
class ListMenusByStoreService(
    private val repo: MenuRepository
) : ListMenusByStoreUseCase {
    override fun handle(storeIdentifier: UUID): List<Menu> =
        repo.findAllByStore(storeIdentifier)
}