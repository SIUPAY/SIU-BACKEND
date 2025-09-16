package siu.siubackend.menu.application.service

import org.springframework.stereotype.Service
import siu.siubackend.menu.application.port.input.ListMenusUseCase
import siu.siubackend.menu.application.port.output.MenuRepository
import siu.siubackend.menu.domain.Menu

@Service
class ListMenusService(
    private val repo: MenuRepository
) : ListMenusUseCase {
    override fun handle(): List<Menu> = repo.findAll()
}