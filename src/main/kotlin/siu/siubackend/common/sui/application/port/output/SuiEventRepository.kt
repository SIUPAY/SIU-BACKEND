package siu.siubackend.common.sui.application.port.output

import siu.siubackend.common.sui.domain.SuiPaidEvent

interface SuiEventRepository {
    /**
     * Sui 체인에서 특정 컨트랙트의 이벤트들을 조회
     */
    fun queryEvents(
        contractAddress: String,
        eventType: String,
        fromTimestamp: Long? = null,
        limit: Int = 100
    ): List<SuiPaidEvent>
    
    /**
     * 특정 트랜잭션의 이벤트 조회
     */
    fun getEventsByTransaction(transactionDigest: String): List<SuiPaidEvent>
}
