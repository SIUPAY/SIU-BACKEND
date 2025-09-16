package siu.siubackend.store.application.port.input

import java.util.*

interface GetStoreUseCase {
    fun getStore(storeIdentifier: UUID): siu.siubackend.store.domain.Store?
}
