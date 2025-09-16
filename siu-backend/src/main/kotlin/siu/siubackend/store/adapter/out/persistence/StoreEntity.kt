package siu.siubackend.store.adapter.out.persistence

import jakarta.persistence.*
import siu.siubackend.store.domain.Store
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "stores")
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

    companion object {
        fun Store.toEntity(): StoreEntity {
            return StoreEntity(
                identifier = this.identifier,
                name = this.name,
                address = this.address,
                phone = this.phone,
                profileImgUrl = this.profileImgUrl,
                walletAddress = this.walletAddress,
                createdDate = this.createdDate
            )
        }
    }
}
