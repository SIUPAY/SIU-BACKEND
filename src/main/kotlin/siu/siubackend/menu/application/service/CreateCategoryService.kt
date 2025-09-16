package siu.siubackend.menu.application.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import siu.siubackend.menu.application.port.input.CreateCategoryUseCase
import siu.siubackend.menu.application.port.output.CategoryRepository
import siu.siubackend.menu.domain.Category
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
            name = cmd.name.trim()
        )
        return repo.save(category).identifier
    }
}


