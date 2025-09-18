package siu.siubackend.menu.application.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import siu.siubackend.menu.application.port.input.CreateCategoryUseCase
import siu.siubackend.menu.application.port.output.CategoryRepository
import siu.siubackend.menu.domain.Category
import java.time.OffsetDateTime
import java.util.*

@Service
class CreateCategoryService(
    private val repo: CategoryRepository
) : CreateCategoryUseCase {

    @Transactional
    override fun handle(cmd: CreateCategoryUseCase.Command): UUID {
        require(cmd.name.isNotBlank()) { "카테고리명은 비어있을 수 없습니다." }
        val category = Category(
            identifier = UUID.randomUUID(),
            storeIdentifier = cmd.storeIdentifier,
            name = cmd.name.trim(),
            description = cmd.description,
            displayOrder = cmd.displayOrder,
            createdAt = OffsetDateTime.now()
        )
        return repo.save(category).identifier
    }
}


