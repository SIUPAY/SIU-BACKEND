package siu.siubackend.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "sui")
data class SuiProperties(
    var chain: ChainProperties = ChainProperties()
) {
    data class ChainProperties(
        var rpcUrl: String = "",
        var contractAddress: String = "",
        var eventModuleName: String = "payment",
        var eventPolling: EventPollingProperties = EventPollingProperties(),
        var websocket: WebSocketProperties = WebSocketProperties()
    )
    
    data class EventPollingProperties(
        var enabled: Boolean = true,
        var intervalSeconds: Long = 10,
        var batchSize: Int = 100
    )
    
    data class WebSocketProperties(
        var enabled: Boolean = false,
        var url: String = "",
        var reconnect: ReconnectProperties = ReconnectProperties()
    )
    
    data class ReconnectProperties(
        var maxAttempts: Int = 10,
        var initialDelay: Long = 1000,
        var maxDelay: Long = 30000,
        var backoffMultiplier: Double = 2.0
    )
}
