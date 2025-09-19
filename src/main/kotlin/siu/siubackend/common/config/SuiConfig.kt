package siu.siubackend.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@Configuration
@EnableScheduling
class SuiConfig(
    private val suiProperties: SuiProperties
) {

    @Bean
    fun suiWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(suiProperties.chain.rpcUrl)
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) // 10MB
            }
            .build()
    }

    @Bean
    fun suiHttpClient(): okhttp3.OkHttpClient {
        return okhttp3.OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(10))
            .readTimeout(Duration.ofSeconds(30))
            .writeTimeout(Duration.ofSeconds(30))
            .build()
    }
}
