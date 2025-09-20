package siu.siubackend.store.adapter.out.persistence

import org.springframework.stereotype.Repository
import siu.siubackend.store.adapter.out.persistence.StoreEntity.Companion.toEntity
import siu.siubackend.store.application.port.input.SearchStoreResult
import siu.siubackend.store.application.port.output.StoreRepository
import siu.siubackend.store.domain.Location
import siu.siubackend.store.domain.Store
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Repository
class StoreRepositoryImpl(
    private val storeJpaRepository: StoreJpaRepository
) : StoreRepository {

    override fun save(store: Store): Store {
        val storeEntity = store.toEntity()
        val savedEntity = storeJpaRepository.save(storeEntity)
        return savedEntity.toDomain()
    }

    override fun findById(identifier: UUID): Store? {
        return storeJpaRepository.findById(identifier)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByWalletAddress(walletAddress: String): Store? {
        return storeJpaRepository.findByWalletAddress(walletAddress)?.toDomain()
    }

    override fun findAll(): List<Store> {
        return storeJpaRepository.findAll().map { it.toDomain() }
    }

    override fun deleteById(identifier: UUID) {
        storeJpaRepository.deleteById(identifier)
    }

    override fun searchStoresWithinDistance(
        location: Location,
        distance: Int,
        query: String?
    ): List<SearchStoreResult> {
        val results = storeJpaRepository.findStoresWithinDistance(
            location.latitude,
            location.longitude,
            distance,
            query
        )

        return results.map { row ->
            val store = mapRowToStore(row)
            val distanceValue = (row[9] as? Number)?.toDouble() ?: 0.0

            SearchStoreResult(
                store = store,
                distance = distanceValue
            )
        }
    }

    private fun mapRowToStore(row: Array<Any>): Store {
        return Store(
            identifier = when (val id = row[0]) {
                is UUID -> id
                is String -> UUID.fromString(id)
                else -> throw IllegalArgumentException("Invalid identifier type: ${id::class}")
            },
            name = row[2].toString(),
            address = row[3].toString(),
            phone = row[4]?.toString(),
            profileImgUrl = row[5].toString(),
            walletAddress = row[6].toString(),
            location = Location(latitude = 0.0, longitude = 0.0), // 검색 결과에서는 위치 정보 불필요
            totalOrderCount = when (val count = row[7]) {
                is Number -> count.toInt()
                else -> 0
            },
            createdDate = when (val date = row[8]) {
                is Instant -> date.atOffset(ZoneOffset.UTC)
                is java.sql.Timestamp -> date.toInstant().atOffset(ZoneOffset.UTC)
                else -> OffsetDateTime.now()
            }
        )
    }
}
