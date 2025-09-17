package siu.siubackend.store.application.port.input

import siu.siubackend.store.domain.Store
import java.util.*

interface GetStoreUseCase {
    fun getStore(storeIdentifier: UUID): Store
}
