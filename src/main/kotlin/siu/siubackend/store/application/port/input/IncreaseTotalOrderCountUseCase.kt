package siu.siubackend.store.application.port.input

import java.util.*

interface IncreaseTotalOrderCountUseCase {
    fun increaseTotalOrderCount(storeId: UUID)
}
