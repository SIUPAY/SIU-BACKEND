package siu.siubackend.common.sui.adapter.out.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.*
import okhttp3.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import siu.siubackend.common.config.SuiProperties
import siu.siubackend.common.sui.domain.SuiPaidEvent
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

@Component
class SuiWebSocketClient(
    private val suiHttpClient: OkHttpClient,
    private val suiProperties: SuiProperties,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(SuiWebSocketClient::class.java)
    
    private var webSocket: WebSocket? = null
    private val isConnected = AtomicBoolean(false)
    private val isConnecting = AtomicBoolean(false)
    private val reconnectAttempts = AtomicInteger(0)
    private val subscriptionId = AtomicLong(0)
    
    private var eventHandler: ((SuiPaidEvent) -> Unit)? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun setEventHandler(handler: (SuiPaidEvent) -> Unit) {
        this.eventHandler = handler
    }

    fun connect() {
        if (isConnecting.get() || isConnected.get()) {
            logger.debug("Already connecting or connected, skipping connect request")
            return
        }

        if (!suiProperties.chain.websocket.enabled) {
            logger.info("WebSocket is disabled in configuration")
            return
        }

        isConnecting.set(true)
        
        try {
            val request = Request.Builder()
                .url(suiProperties.chain.websocket.url)
                .build()

            webSocket = suiHttpClient.newWebSocket(request, createWebSocketListener())
            logger.info("Initiating WebSocket connection to: {}", suiProperties.chain.websocket.url)
            
        } catch (e: Exception) {
            logger.error("Failed to initiate WebSocket connection", e)
            isConnecting.set(false)
            scheduleReconnect()
        }
    }

    fun disconnect() {
        logger.info("Disconnecting WebSocket...")
        isConnected.set(false)
        isConnecting.set(false)
        reconnectAttempts.set(0)
        
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
        
        coroutineScope.cancel()
    }

    private fun createWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                logger.info("WebSocket connected successfully")
                isConnected.set(true)
                isConnecting.set(false)
                reconnectAttempts.set(0)
                
                // 연결 성공 시 이벤트 구독
                subscribeToEvents()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                logger.debug("Received WebSocket message: {}", text)
                handleMessage(text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                logger.warn("WebSocket closing: code={}, reason={}", code, reason)
                isConnected.set(false)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                logger.warn("WebSocket closed: code={}, reason={}", code, reason)
                isConnected.set(false)
                isConnecting.set(false)
                
                // 정상 종료가 아닌 경우 재연결 시도
                if (code != 1000) {
                    scheduleReconnect()
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                logger.error("WebSocket connection failed", t)
                isConnected.set(false)
                isConnecting.set(false)
                
                scheduleReconnect()
            }
        }
    }

    private fun subscribeToEvents() {
        try {
            val subscriptionRequest = mapOf(
                "jsonrpc" to "2.0",
                "id" to 1,
                "method" to "suix_subscribeEvent",
                "params" to listOf(
                    mapOf(
                        "MoveEventType" to "${suiProperties.chain.contractAddress}::payment::PaidEvent"
                    )
                )
            )

            val requestJson = objectMapper.writeValueAsString(subscriptionRequest)
            logger.info("Subscribing to PaidEvents: {}", requestJson)
            
            val success = webSocket?.send(requestJson) ?: false
            if (!success) {
                logger.error("Failed to send subscription request")
                scheduleReconnect()
            }
            
        } catch (e: Exception) {
            logger.error("Error subscribing to events", e)
            scheduleReconnect()
        }
    }

    private fun handleMessage(message: String) {
        try {
            @Suppress("UNCHECKED_CAST")
            val response = objectMapper.readValue(message, Map::class.java) as Map<String, Any>
            
            when {
                response.containsKey("result") -> {
                    // 구독 응답
                    val subscriptionIdValue = response["result"]
                    subscriptionId.set((subscriptionIdValue as Number).toLong())
                    logger.info("Successfully subscribed to events with subscription ID: {}", subscriptionIdValue)
                }
                
                response.containsKey("params") -> {
                    // 이벤트 알림
                    handleEventNotification(response)
                }
                
                response.containsKey("error") -> {
                    val error = response["error"]
                    logger.error("Received error from Sui WebSocket: {}", error)
                }
                
                else -> {
                    logger.debug("Received unknown message type: {}", message)
                }
            }
            
        } catch (e: Exception) {
            logger.error("Error handling WebSocket message: {}", message, e)
        }
    }

    private fun handleEventNotification(response: Map<String, Any>) {
        try {
            @Suppress("UNCHECKED_CAST")
            val params = response["params"] as? Map<String, Any>
            @Suppress("UNCHECKED_CAST")
            val result = params?.get("result") as? Map<String, Any>
            
            if (result != null && isPaidEvent(result)) {
                val paidEvent = SuiPaidEvent.fromSuiEvent(result)
                logger.info("Received PaidEvent: order={}, tx={}", paidEvent.orderIdentifier, paidEvent.transactionDigest)
                
                // 이벤트 핸들러 호출
                eventHandler?.invoke(paidEvent)
            }
            
        } catch (e: Exception) {
            logger.error("Error processing event notification", e)
        }
    }

    private fun isPaidEvent(event: Map<String, Any>): Boolean {
        val type = event["type"] as? String
        return type?.contains("PaidEvent") == true
    }

    private fun scheduleReconnect() {
        if (reconnectAttempts.get() >= suiProperties.chain.websocket.reconnect.maxAttempts) {
            logger.error("Max reconnection attempts ({}) reached, giving up", 
                suiProperties.chain.websocket.reconnect.maxAttempts)
            return
        }

        val attempt = reconnectAttempts.incrementAndGet()
        val delay = calculateReconnectDelay(attempt)
        
        logger.info("Scheduling reconnection attempt {} in {} ms", attempt, delay)
        
        coroutineScope.launch {
            delay(delay)
            if (!isConnected.get()) {
                logger.info("Attempting to reconnect (attempt {})", attempt)
                connect()
            }
        }
    }

    private fun calculateReconnectDelay(attempt: Int): Long {
        val baseDelay = suiProperties.chain.websocket.reconnect.initialDelay
        val multiplier = suiProperties.chain.websocket.reconnect.backoffMultiplier
        val maxDelay = suiProperties.chain.websocket.reconnect.maxDelay
        
        val delay = (baseDelay * Math.pow(multiplier, (attempt - 1).toDouble())).toLong()
        val jitter = Random.nextLong(-delay / 10, delay / 10) // ±10% jitter
        
        return minOf(delay + jitter, maxDelay)
    }

    fun isConnected(): Boolean = isConnected.get()
    
    fun getSubscriptionId(): Long = subscriptionId.get()
}
