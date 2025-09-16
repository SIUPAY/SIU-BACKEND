package siu.siubackend.store.adapter.out.persistence

import jakarta.persistence.*
import siu.siubackend.store.domain.Store
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "store")
data class StoreEntity(
    @Id
    @Column(name = "identifier")
    val identifier: UUID,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "address", nullable = false)
    var address: String,

    @Column(name = "phone")
    var phone: String?,

    @Column(name = "profile_img_url", nullable = false)
    var profileImgUrl: String,

    @Column(name = "wallet_address", nullable = false)
    var walletAddress: String,

    @Column(name = "created_date", nullable = false)
    val createdDate: OffsetDateTime
) {
    companion object {
        fun fromDomain(store: Store, userIdentifier: UUID): StoreEntity {
            return StoreEntity(
                identifier = store.identifier,
                name = store.name,
                address = store.address,
                phone = store.phone,
                profileImgUrl = store.profileImgUrl,
                walletAddress = store.walletAddress,
                createdDate = store.createdDate
            )
        }
    }

    fun toDomain(): Store {
        return Store(
            identifier = identifier,
            name = name,
            address = address,
            phone = phone,
            profileImgUrl = profileImgUrl,
            walletAddress = walletAddress,
            createdDate = createdDate
        )
    }
}

fun Store.toEntity(userIdentifier: UUID): StoreEntity {
    return StoreEntity.fromDomain(this, userIdentifier)
}
