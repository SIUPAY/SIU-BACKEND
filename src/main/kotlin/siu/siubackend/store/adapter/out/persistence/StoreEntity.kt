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

    @Column(name = "location", nullable = false, columnDefinition = "geography(Point, 4326)")
    val location: Point,

    @Column(name = "total_order_count", nullable = false)
    var totalOrderCount: Int,

    @Column(name = "created_date", nullable = false)
    val createdDate: OffsetDateTime
) {
    companion object {
        fun Store.toEntity(): StoreEntity {
            return StoreEntity(
                identifier = this.identifier,
                name = this.name,
                address = this.address,
                phone = this.phone,
                profileImgUrl = this.profileImgUrl,
                walletAddress = this.walletAddress,
                location = this.location.toGeometryPoint(),
                totalOrderCount = this.totalOrderCount,
                createdDate = this.createdDate
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
            location = location.toStoreLocation(),
            totalOrderCount = totalOrderCount,
            createdDate = createdDate
        )
    }
}
