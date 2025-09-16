package siu.siubackend.menu.application.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import siu.siubackend.menu.application.port.input.CreateMenuUseCase
import siu.siubackend.menu.application.port.output.ImageUploader
import siu.siubackend.menu.application.port.output.MenuRepository
import siu.siubackend.menu.domain.service.MenuFactory
import java.util.*

@Service
class CreateMenuService(
    private val repo: MenuRepository,
    private val uploader: ImageUploader
) : CreateMenuUseCase {

    @Transactional
    override fun handle(cmd: CreateMenuUseCase.Command): UUID {
        val url = cmd.imageFileBytes?.let { uploader.upload(it, cmd.imageOriginalName) }
        val menu = MenuFactory.create(
            storeIdentifier = cmd.storeIdentifier,
            categoryIdentifier = cmd.categoryIdentifier,
            name = cmd.name,
            price = cmd.price,
            description = cmd.description,
            imageUrl = url
        )
        return repo.save(menu).identifier
    }
}