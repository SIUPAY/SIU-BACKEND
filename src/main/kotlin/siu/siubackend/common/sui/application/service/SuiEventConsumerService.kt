package siu.siubackend.common.sui.application.service

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import siu.siubackend.common.checkpoint.application.port.output.ChainEventCheckpointRepository
import siu.siubackend.common.checkpoint.domain.ChainEventCheckpoint
import siu.siubackend.common.config.SuiProperties
import siu.siubackend.common.sui.application.port.output.SuiEventRepository
import siu.siubackend.common.sui.domain.SuiPaidEvent
import siu.siubackend.order.application.port.input.RecordOrderSettlementUseCase

@Service
class SuiEventConsumerService(
    private val suiEventRepository: SuiEventRepository,
    private val checkpointRepository: ChainEventCheckpointRepository,
    private val recordOrderSettlementUseCase: RecordOrderSettlementUseCase,
    private val suiProperties: SuiProperties
) {

    private val logger = LoggerFactory.getLogger(SuiEventConsumerService::class.java)
    private val chainName = "sui"

    @Scheduled(fixedDelayString = "#{@suiProperties.chain.eventPolling.intervalSeconds * 1000}")
    fun consumePaidEvents() {
        // WebSocket이 활성화된 경우 폴링 비활성화 (백업용으로만 사용)
        if (!suiProperties.chain.eventPolling.enabled || suiProperties.chain.websocket.enabled) {
            return
        }

        try {
            logger.debug("Starting Sui event consumption cycle")
            
            val checkpoint = getOrCreateCheckpoint()
            val events = fetchNewEvents(checkpoint)
            
            if (events.isNotEmpty()) {
                logger.info("Found {} new PaidEvents to process", events.size)
                processEvents(events, checkpoint)
            } else {
                logger.debug("No new events found")
            }
            
        } catch (e: Exception) {
            logger.error("Error during Sui event consumption", e)
        }
    }

    private fun getOrCreateCheckpoint(): ChainEventCheckpoint {
        return checkpointRepository.findByChainAndContract(chainName, suiProperties.chain.contractAddress)
            ?: run {
                logger.info("Creating new checkpoint for Sui contract: {}", suiProperties.chain.contractAddress)
                val newCheckpoint = ChainEventCheckpoint.create(
                    chainName = chainName,
                    contractAddress = suiProperties.chain.contractAddress,
                    lastProcessedTimestamp = 0 // 처음부터 시작
                )
                checkpointRepository.save(newCheckpoint)
            }
    }

    private fun fetchNewEvents(checkpoint: ChainEventCheckpoint): List<SuiPaidEvent> {
        return try {
            // 타입 필터를 비우고 패키지 단위로 조회한 다음, 내부에서 유효 이벤트를 선별
            suiEventRepository.queryEvents(
                contractAddress = suiProperties.chain.contractAddress,
                eventType = "",
                fromTimestamp = checkpoint.lastProcessedTimestamp,
                limit = suiProperties.chain.eventPolling.batchSize
            ).filter { event ->
                // 중복 방지: 이미 처리한 타임스탬프보다 이후 이벤트만
                event.timestamp > checkpoint.lastProcessedTimestamp
            }.sortedBy { it.timestamp } // 시간 순으로 정렬하여 순차 처리
        } catch (e: Exception) {
            logger.error("Failed to fetch new events", e)
            emptyList()
        }
    }

    private fun processEvents(events: List<SuiPaidEvent>, checkpoint: ChainEventCheckpoint) {
        var latestTimestamp = checkpoint.lastProcessedTimestamp
        var latestBlockNumber = checkpoint.lastProcessedBlockNumber
        var processedCount = 0

        for (event in events) {
            try {
                processPaymentEvent(event)
                
                // 진행 상황 업데이트
                latestTimestamp = maxOf(latestTimestamp, event.timestamp)
                latestBlockNumber = maxOf(latestBlockNumber, event.blockNumber)
                processedCount++
                
                logger.debug("Successfully processed event: {} for order: {}", 
                    event.eventId, event.orderIdentifier)
                    
            } catch (e: Exception) {
                logger.error("Failed to process event: {} for order: {}", 
                    event.eventId, event.orderIdentifier, e)
                // 개별 이벤트 실패 시에도 계속 진행 (skip strategy)
            }
        }

        // 체크포인트 업데이트
        if (processedCount > 0) {
            val updatedCheckpoint = checkpoint.updateProgress(latestBlockNumber, latestTimestamp)
            checkpointRepository.saveOrUpdate(updatedCheckpoint)
            
            logger.info("Processed {} events, updated checkpoint to timestamp: {}", 
                processedCount, latestTimestamp)
        }
    }

    private fun processPaymentEvent(event: SuiPaidEvent) {
        val command = RecordOrderSettlementUseCase.Command.fromSuiEvent(
            orderIdentifier = event.orderIdentifier,
            txId = event.transactionDigest,
            toWalletAddress = event.toWalletAddress,
            fromWalletAddress = event.fromWalletAddress,
            totalBrokerageFeeInMist = event.totalBrokerageFee,
            totalCryptoAmountInMist = event.totalCryptoAmount,
            updateOrderPaymentStatus = true
        )

        val result = recordOrderSettlementUseCase.record(command)
        
        logger.info("Recorded order settlement for order: {} with tx: {}", 
            result.orderIdentifier, result.txId)
    }
    
    /**
     * 수동으로 특정 트랜잭션의 이벤트를 처리하는 메서드 (테스트/디버깅용)
     */
    fun processTransactionManually(transactionDigest: String) {
        try {
            logger.info("Manually processing transaction: {}", transactionDigest)
            
            val events = suiEventRepository.getEventsByTransaction(transactionDigest)
            if (events.isEmpty()) {
                logger.warn("No PaidEvents found in transaction: {}", transactionDigest)
                return
            }

            for (event in events) {
                processPaymentEvent(event)
            }
            
            logger.info("Successfully processed {} events from transaction: {}", 
                events.size, transactionDigest)
                
        } catch (e: Exception) {
            logger.error("Failed to manually process transaction: {}", transactionDigest, e)
            throw e
        }
    }
}
