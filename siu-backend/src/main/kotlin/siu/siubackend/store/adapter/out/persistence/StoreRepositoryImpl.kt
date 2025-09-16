package siu.siubackend.store.adapter.out.persistence

import org.springframework.stereotype.Repository
import siu.siubackend.store.application.port.input.SearchStoreResult
import siu.siubackend.store.application.port.output.StoreRepository
import siu.siubackend.store.domain.Location
import siu.siubackend.store.domain.Store
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Repository
class StoreRepositoryImpl(
    private val storeJpaRepository: StoreJpaRepository
) : StoreRepository {

    override fun save(store: Store, userIdentifier: UUID): Store {
        val storeEntity = store.toEntity(userIdentifier)
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
            val distanceValue = (row[row.size - 2] as BigDecimal).toDouble()
            val totalOrderCount = (row[row.size - 1] as BigDecimal).toInt()

            SearchStoreResult(
                store = store,
                distance = distanceValue,
                totalOrderCount = totalOrderCount
            )
        }
    }

    private fun mapRowToStore(row: Array<Any>): Store {
        return Store(
            identifier = row[0] as UUID,
            name = row[2] as String,
            address = row[3] as String,
            phone = row[4] as String?,
            profileImgUrl = row[5] as String,
            walletAddress = row[6] as String,
            location = null, // location은 검색 결과에서 불필요하므로 null로 설정
            createdDate = (row[8] as Timestamp).toInstant().atOffset(ZoneOffset.UTC)
        )
    }
}
