package siu.siubackend.store.domain.service

import siu.siubackend.store.domain.Location
import siu.siubackend.store.domain.Store
import java.time.OffsetDateTime
import java.util.*

object StoreFactory {
    fun create(
        name: String,
        address: String,
        phone: String?,
        profileImgUrl: String,
        walletAddress: String,
        location: Location?
    ): Store {
        return Store(
            identifier = UUID.randomUUID(),
            name = name,
            address = address,
            phone = phone,
            profileImgUrl = profileImgUrl,
            walletAddress = walletAddress,
            location = location,
            createdDate = OffsetDateTime.now()
        )
    }
}
