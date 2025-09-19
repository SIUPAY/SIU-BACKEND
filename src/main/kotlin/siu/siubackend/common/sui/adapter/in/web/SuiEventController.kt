package siu.siubackend.common.sui.adapter.`in`.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.common.sui.application.service.SuiEventConsumerService
import siu.siubackend.common.sui.application.service.SuiRealtimeEventConsumer

@RestController
@RequestMapping("/api/admin/sui-events")
@Tag(name = "Sui Events Admin", description = "Sui 체인 이벤트 관리 API")
class SuiEventController(
    private val suiEventConsumerService: SuiEventConsumerService,
    private val suiRealtimeEventConsumer: SuiRealtimeEventConsumer
) {

    @PostMapping("/process-transaction/{transactionDigest}")
    @Operation(summary = "특정 트랜잭션의 이벤트 수동 처리", description = "특정 트랜잭션 다이제스트의 PaidEvent를 수동으로 처리합니다.")
    fun processTransaction(
        @PathVariable transactionDigest: String
    ): ResponseEntity<Map<String, String>> {
        return try {
            runBlocking {
                suiRealtimeEventConsumer.processTransactionManually(transactionDigest)
            }
            ResponseEntity.ok(mapOf(
                "message" to "Transaction processed successfully",
                "transactionDigest" to transactionDigest
            ))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf(
                "error" to "Failed to process transaction",
                "message" to (e.message ?: "Unknown error"),
                "transactionDigest" to transactionDigest
            ))
        }
    }

    @PostMapping("/trigger-consumption")
    @Operation(summary = "백업 폴링 수동 트리거", description = "WebSocket이 끊어진 경우 백업 폴링을 수동으로 실행합니다.")
    fun triggerConsumption(): ResponseEntity<Map<String, String>> {
        return try {
            suiRealtimeEventConsumer.backupEventPolling()
            ResponseEntity.ok(mapOf(
                "message" to "Backup event polling triggered successfully"
            ))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf(
                "error" to "Failed to trigger backup polling",
                "message" to (e.message ?: "Unknown error")
            ))
        }
    }

    @GetMapping("/connection-status")
    @Operation(summary = "WebSocket 연결 상태 확인", description = "현재 WebSocket 연결 상태와 구독 정보를 확인합니다.")
    fun getConnectionStatus(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(suiRealtimeEventConsumer.getConnectionStatus())
    }
}
