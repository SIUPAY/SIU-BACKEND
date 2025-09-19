package siu.siubackend.common.sui.adapter.out.http

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import siu.siubackend.common.config.SuiProperties
import siu.siubackend.common.sui.application.port.output.SuiEventRepository
import siu.siubackend.common.sui.domain.SuiPaidEvent

@Repository
class SuiEventRepositoryImpl(
    private val suiHttpClient: OkHttpClient,
    private val suiProperties: SuiProperties,
    private val objectMapper: ObjectMapper
) : SuiEventRepository {

    private val logger = LoggerFactory.getLogger(SuiEventRepositoryImpl::class.java)
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    override fun queryEvents(
        contractAddress: String,
        eventType: String,
        fromTimestamp: Long?,
        limit: Int
    ): List<SuiPaidEvent> {
        try {
            val requestBody = buildEventQueryRequest(contractAddress, eventType, fromTimestamp, limit)
            val request = Request.Builder()
                .url(suiProperties.chain.rpcUrl)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .build()

            suiHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    logger.error("Failed to query Sui events: HTTP ${response.code}")
                    return emptyList()
                }

                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty()) {
                    logger.warn("Empty response from Sui RPC")
                    return emptyList()
                }

                return parseEventsResponse(responseBody)
            }
        } catch (e: Exception) {
            logger.error("Error querying Sui events", e)
            return emptyList()
        }
    }

    override fun getEventsByTransaction(transactionDigest: String): List<SuiPaidEvent> {
        try {
            val requestBody = buildTransactionEventRequest(transactionDigest)
            val request = Request.Builder()
                .url(suiProperties.chain.rpcUrl)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .build()

            suiHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    logger.error("Failed to get transaction events: HTTP ${response.code}")
                    return emptyList()
                }

                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty()) {
                    return emptyList()
                }

                return parseTransactionEventsResponse(responseBody)
            }
        } catch (e: Exception) {
            logger.error("Error getting transaction events", e)
            return emptyList()
        }
    }

    private fun buildEventQueryRequest(
        contractAddress: String,
        eventType: String,
        fromTimestamp: Long?,
        limit: Int
    ): RequestBody {
        // EventFilter 조합: MoveEventModule(or MoveEventType) (+ TimeRange)
        val filters = mutableListOf<Map<String, Any>>()
        
        if (eventType.isNotEmpty()) {
            filters.add(mapOf("MoveEventType" to eventType))
        } else {
            filters.add(mapOf("MoveEventModule" to mapOf(
                "package" to contractAddress,
                "module" to suiProperties.chain.eventModuleName
            )))
        }
        
        // TimeRange 필터를 사용하지 않음 (All 필터와 호환성 문제)
        // 대신 애플리케이션 레벨에서 timestamp 필터링
        
        val filter: Map<String, Any> = if (filters.size == 1) filters.first() else mapOf("All" to filters)

        val requestMap = mapOf(
            "jsonrpc" to "2.0",
            "id" to 1,
            "method" to "suix_queryEvents",
            "params" to listOf(
                filter,
                null, // cursor
                limit,
                false  // ascending order (오래된 것부터)
            )
        )

        val json = objectMapper.writeValueAsString(requestMap)
        logger.debug("Sui RPC request: {}", json)
        
        return RequestBody.create(JSON_MEDIA_TYPE, json)
    }

    private fun buildTransactionEventRequest(transactionDigest: String): RequestBody {
        val requestMap = mapOf(
            "jsonrpc" to "2.0",
            "id" to 1,
            "method" to "sui_getTransactionBlock",
            "params" to listOf(
                transactionDigest,
                mapOf(
                    "showEvents" to true,
                    "showInput" to false,
                    "showEffects" to false,
                    "showObjectChanges" to false,
                    "showBalanceChanges" to false
                )
            )
        )

        val json = objectMapper.writeValueAsString(requestMap)
        return RequestBody.create(JSON_MEDIA_TYPE, json)
    }

    private fun parseEventsResponse(responseBody: String): List<SuiPaidEvent> {
        try {
            println("DEBUG: RPC Response Body: $responseBody")
            
            val response = objectMapper.readValue(responseBody, Map::class.java) as Map<String, Any>
            val result = response["result"] as? Map<String, Any>
            if (result == null) {
                println("DEBUG: No result in response")
                return emptyList()
            }
            
            val data = result["data"] as? List<Map<String, Any>>
            if (data == null) {
                println("DEBUG: No data in result")
                return emptyList()
            }
            
            println("DEBUG: Found ${data.size} events in response")

            return data.mapNotNull { eventWrapper ->
                try {
                    println("DEBUG: Processing event: $eventWrapper")
                    // suix_queryEvents의 각 항목 자체가 이벤트 맵 구조(type, parsedJson, id, timestampMs ...)
                    // 그대로 파싱 시도
                    val paidEvent = SuiPaidEvent.fromSuiEventFlexible(eventWrapper)
                    if (paidEvent != null) {
                        println("DEBUG: Successfully parsed event into SuiPaidEvent")
                    }
                    paidEvent
                } catch (e: Exception) {
                    logger.warn("Failed to parse event: ${e.message}")
                    println("DEBUG: Failed to parse event: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to parse events response", e)
            println("DEBUG: Exception in parseEventsResponse: ${e.message}")
            return emptyList()
        }
    }

    private fun parseTransactionEventsResponse(responseBody: String): List<SuiPaidEvent> {
        try {
            val response = objectMapper.readValue(responseBody, Map::class.java) as Map<String, Any>
            val result = response["result"] as? Map<String, Any> ?: return emptyList()
            val events = result["events"] as? List<Map<String, Any>> ?: return emptyList()

            return events.mapNotNull { event ->
                try {
                    SuiPaidEvent.fromSuiEventFlexible(event)
                } catch (e: Exception) {
                    logger.warn("Failed to parse transaction event: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to parse transaction events response", e)
            return emptyList()
        }
    }

    private fun isPaidEvent(event: Map<String, Any>): Boolean {
        val type = event["type"] as? String
        return type?.contains("PaidEvent") == true
    }
}
