package siu.siubackend.menu.application.port.input

import java.util.*

interface CreateMenuUseCase {
    fun handle(cmd: Command): UUID
    data class Command(
        val storeIdentifier: UUID,
        val categoryIdentifier: UUID?,
        val name: String,
        val price: Int,
        val description: String?,
        val imageFileBytes: ByteArray?,  // nullable: 파일 없을 수 있음
        val imageOriginalName: String?
    )
}