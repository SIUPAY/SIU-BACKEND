package siu.siubackend.menu.application.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import siu.siubackend.menu.application.port.input.UpdateMenuUseCase
import siu.siubackend.menu.application.port.output.ImageUploader
import siu.siubackend.menu.application.port.output.MenuRepository
import java.util.*
import kotlin.NoSuchElementException

@Service
class UpdateMenuService(
    private val repo: MenuRepository,
    private val uploader: ImageUploader
) : UpdateMenuUseCase {

    @Transactional
    override fun handle(id: UUID, cmd: UpdateMenuUseCase.Command) {
        val current = repo.findById(id) ?: throw NoSuchElementException("Menu not found: $id")
        val newUrl = cmd.imageFileBytes?.let { uploader.upload(it, cmd.imageOriginalName) }
            ?: current.imageUrl

        val updated = current.update(
            name = cmd.name,
            price = cmd.price,
            description = cmd.description,
            categoryIdentifier = cmd.categoryIdentifier,
            imageUrl = newUrl
        )
        repo.save(updated)
    }
}