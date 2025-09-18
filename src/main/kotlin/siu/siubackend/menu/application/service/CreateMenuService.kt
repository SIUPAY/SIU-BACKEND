package siu.siubackend.menu.application.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import siu.siubackend.common.exception.ExceptionUtils
import siu.siubackend.menu.application.port.input.CreateMenuUseCase
import siu.siubackend.menu.application.port.output.ImageUploader
import siu.siubackend.menu.application.port.output.MenuRepository
import siu.siubackend.menu.domain.Menu
import siu.siubackend.menu.domain.service.MenuFactory
import java.util.*

@Service
class CreateMenuService(
    private val repo: MenuRepository,
    private val uploader: ImageUploader
) : CreateMenuUseCase {

    @Transactional
    override fun handle(cmd: CreateMenuUseCase.Command): Menu {
        validateCommand(cmd)
        
        val url = cmd.imageFileBytes?.let { uploader.upload(it, cmd.imageOriginalName) }
        val menu = MenuFactory.create(
            storeIdentifier = cmd.storeIdentifier,
            categoryIdentifier = cmd.categoryIdentifier,
            name = cmd.name,
            price = cmd.price,
            description = cmd.description,
            imageUrl = url,
            isAvailable = cmd.isAvailable ?: true
        )
        return repo.save(menu)
    }
    
    private fun validateCommand(cmd: CreateMenuUseCase.Command) {
        if (cmd.name.isBlank()) {
            ExceptionUtils.throwValidationError("메뉴 이름은 필수입니다")
        }
        if (cmd.price < 0) {
            ExceptionUtils.throwValidationError("메뉴 가격은 0원 이상이어야 합니다")
        }
    }
}