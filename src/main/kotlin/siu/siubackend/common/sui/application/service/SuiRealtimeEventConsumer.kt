package siu.siubackend.common.sui.application.service

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import siu.siubackend.common.checkpoint.application.port.output.ChainEventCheckpointRepository
import siu.siubackend.common.checkpoint.domain.ChainEventCheckpoint
import siu.siubackend.common.config.SuiProperties
import siu.siubackend.common.sui.adapter.out.websocket.SuiWebSocketClient
import siu.siubackend.common.sui.application.port.output.SuiEventRepository
import siu.siubackend.common.sui.domain.SuiPaidEvent
import siu.siubackend.order.application.port.input.RecordOrderSettlementUseCase
import java.util.concurrent.ConcurrentHashMap

@Service
class SuiRealtimeEventConsumer(
    private val webSocketClient: SuiWebSocketClient,
    private val suiEventRepository: SuiEventRepository,
    private val checkpointRepository: ChainEventCheckpointRepository,
    private val recordOrderSettlementUseCase: RecordOrderSettlementUseCase,
    private val suiProperties: SuiProperties
) {

    private val logger = LoggerFactory.getLogger(SuiRealtimeEventConsumer::class.java)
    private val chainName = "sui"
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    // 중복 처리 방지를 위한 최근 처리된 이벤트 캐시
    private val recentlyProcessedEvents = ConcurrentHashMap<String, Long>()
    private val cacheCleanupInterval = 60 * 60 * 1000L // 1시간

    @PostConstruct
    fun initialize() {
        if (suiProperties.chain.websocket.enabled) {
            logger.info("Initializing Sui realtime event consumer with WebSocket")
            
            // WebSocket 이벤트 핸들러 설정
            webSocketClient.setEventHandler { event ->
                coroutineScope.launch {
                    handleRealtimeEvent(event)
                }
            }
            
            // WebSocket 연결 시작
            webSocketClient.connect()
            
            logger.info("Realtime event consumer initialized successfully")
        } else {
            logger.info("WebSocket is disabled, realtime event consumer will not start")
        }
    }

    @PreDestroy
    fun shutdown() {
        logger.info("Shutting down realtime event consumer")
        webSocketClient.disconnect()
        coroutineScope.cancel()
    }

    private suspend fun handleRealtimeEvent(event: SuiPaidEvent) {
        try {
            val eventKey = "${event.transactionDigest}_${event.eventId}"
            
            // 중복 처리 방지
            if (recentlyProcessedEvents.containsKey(eventKey)) {
                logger.debug("Event already processed recently: {}", eventKey)
                return
            }

            logger.info("Processing realtime PaidEvent: order={}, tx={}", 
                event.orderIdentifier, event.transactionDigest)

            // 이벤트 처리
            processPaymentEvent(event)
            
            // 체크포인트 업데이트
            updateCheckpoint(event)
            
            // 중복 방지 캐시에 추가
            recentlyProcessedEvents[eventKey] = System.currentTimeMillis()
            
            logger.info("Successfully processed realtime event: order={}, tx={}", 
                event.orderIdentifier, event.transactionDigest)
                
        } catch (e: Exception) {
            logger.error("Failed to process realtime event: order={}, tx={}", 
                event.orderIdentifier, event.transactionDigest, e)
        }
    }

    private fun processPaymentEvent(event: SuiPaidEvent) {
        val command = RecordOrderSettlementUseCase.Command(
            orderIdentifier = event.orderIdentifier,
            txId = event.transactionDigest,
            toWalletAddress = event.toWalletAddress,
            fromWalletAddress = event.fromWalletAddress,
            totalBrokerageFee = event.totalBrokerageFee,
            totalCryptoAmount = event.totalCryptoAmount,
            updateOrderPaymentStatus = true
        )

        val result = recordOrderSettlementUseCase.record(command)
        
        logger.info("Recorded order settlement for order: {} with tx: {}", 
            result.orderIdentifier, result.txId)
    }

    private fun updateCheckpoint(event: SuiPaidEvent) {
        try {
            val existingCheckpoint = getOrCreateCheckpoint()
            val updatedCheckpoint = existingCheckpoint.updateProgress(
                blockNumber = maxOf(existingCheckpoint.lastProcessedBlockNumber, event.blockNumber),
                timestamp = maxOf(existingCheckpoint.lastProcessedTimestamp, event.timestamp)
            )
            
            checkpointRepository.saveOrUpdate(updatedCheckpoint)
            logger.debug("Updated checkpoint to timestamp: {}", event.timestamp)
            
        } catch (e: Exception) {
            logger.error("Failed to update checkpoint", e)
        }
    }

    private fun getOrCreateCheckpoint(): ChainEventCheckpoint {
        return checkpointRepository.findByChainAndContract(chainName, suiProperties.chain.contractAddress)
            ?: run {
                logger.info("Creating new checkpoint for WebSocket consumer")
                val newCheckpoint = ChainEventCheckpoint.create(
                    chainName = chainName,
                    contractAddress = suiProperties.chain.contractAddress,
                    lastProcessedTimestamp = 0 // 처음부터 시작
                )
                checkpointRepository.save(newCheckpoint)
            }
    }

    /**
     * 백업 폴링 - WebSocket 연결 문제 시 누락된 이벤트 보정
     */
    @Scheduled(fixedDelayString = "#{@suiProperties.chain.eventPolling.intervalSeconds * 1000}")
    fun backupEventPolling() {
        if (!suiProperties.chain.eventPolling.enabled || webSocketClient.isConnected()) {
            return // 폴링이 비활성화되어 있거나 WebSocket이 정상 연결된 경우 skip
        }

        try {
            logger.debug("Running backup event polling (WebSocket disconnected)")
            
            val checkpoint = getOrCreateCheckpoint()
            val events = suiEventRepository.queryEvents(
                contractAddress = suiProperties.chain.contractAddress,
                eventType = "", // MoveEventModule(package+module) 필터 사용
                fromTimestamp = checkpoint.lastProcessedTimestamp,
                limit = suiProperties.chain.eventPolling.batchSize
            ).filter { event ->
                event.timestamp > checkpoint.lastProcessedTimestamp &&
                !recentlyProcessedEvents.containsKey("${event.transactionDigest}_${event.eventId}")
            }.sortedBy { it.timestamp }

            if (events.isNotEmpty()) {
                logger.info("Found {} events during backup polling", events.size)
                
                events.forEach { event ->
                    coroutineScope.launch {
                        handleRealtimeEvent(event)
                    }
                }
            }
            
        } catch (e: Exception) {
            logger.error("Error during backup event polling", e)
        }
    }

    /**
     * 연결 상태 모니터링 및 재연결
     */
    @Scheduled(fixedDelay = 30000) // 30초마다
    fun monitorConnection() {
        if (!suiProperties.chain.websocket.enabled) {
            return
        }

        if (!webSocketClient.isConnected()) {
            logger.warn("WebSocket is disconnected, attempting to reconnect...")
            webSocketClient.connect()
        } else {
            logger.debug("WebSocket connection is healthy")
        }
    }

    /**
     * 중복 방지 캐시 정리
     */
    @Scheduled(fixedDelay = 300000) // 5분마다
    fun cleanupEventCache() {
        val now = System.currentTimeMillis()
        val expiredKeys = recentlyProcessedEvents.entries
            .filter { now - it.value > cacheCleanupInterval }
            .map { it.key }

        expiredKeys.forEach { recentlyProcessedEvents.remove(it) }
        
        if (expiredKeys.isNotEmpty()) {
            logger.debug("Cleaned up {} expired event cache entries", expiredKeys.size)
        }
    }

    /**
     * 수동으로 특정 트랜잭션의 이벤트를 처리하는 메서드 (테스트/디버깅용)
     */
    suspend fun processTransactionManually(transactionDigest: String) {
        try {
            logger.info("Manually processing transaction: {}", transactionDigest)
            
            val events = suiEventRepository.getEventsByTransaction(transactionDigest)
            if (events.isEmpty()) {
                logger.warn("No PaidEvents found in transaction: {}", transactionDigest)
                return
            }

            for (event in events) {
                handleRealtimeEvent(event)
            }
            
            logger.info("Successfully processed {} events from transaction: {}", 
                events.size, transactionDigest)
                
        } catch (e: Exception) {
            logger.error("Failed to manually process transaction: {}", transactionDigest, e)
            throw e
        }
    }

    fun getConnectionStatus(): Map<String, Any> {
        return mapOf(
            "websocketConnected" to webSocketClient.isConnected(),
            "subscriptionId" to webSocketClient.getSubscriptionId(),
            "recentEventsCount" to recentlyProcessedEvents.size
        )
    }
}
