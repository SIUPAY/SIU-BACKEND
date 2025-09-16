package siu.siubackend.store.adapter.out.persistence

import jakarta.persistence.*
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import siu.siubackend.store.domain.Location
import siu.siubackend.store.domain.Store
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "store")
data class StoreEntity(
    @Id
    @Column(name = "identifier")
    val identifier: UUID,

    @Column(name = "user_identifier", nullable = false)
    val userIdentifier: UUID,

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

    @Column(name = "location", columnDefinition = "geography(Point, 4326)")
    val location: Point?,

    @Column(name = "created_date", nullable = false)
    val createdDate: OffsetDateTime
) {
    companion object {
        fun fromDomain(store: Store, userIdentifier: UUID): StoreEntity {
            return StoreEntity(
                identifier = store.identifier,
                userIdentifier = userIdentifier,
                name = store.name,
                address = store.address,
                phone = store.phone,
                profileImgUrl = store.profileImgUrl,
                walletAddress = store.walletAddress,
                location = store.location?.toGeometryPoint(),
                createdDate = store.createdDate
            )
        }

        fun Point.toStoreLocation(): Location = Location(
            latitude = this.y,
            longitude = this.x
        )

        fun Location.toGeometryPoint(): Point {
            val geometryFactory = GeometryFactory(PrecisionModel(), 4326)
            return geometryFactory.createPoint(Coordinate(longitude, latitude))
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
            location = location?.toStoreLocation(),
            createdDate = createdDate
        )
    }
}

fun Store.toEntity(userIdentifier: UUID): StoreEntity {
    return StoreEntity.fromDomain(this, userIdentifier)
}
