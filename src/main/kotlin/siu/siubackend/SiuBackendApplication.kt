package siu.siubackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import siu.siubackend.common.config.SuiProperties

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(SuiProperties::class)
class SiuBackendApplication

fun main(args: Array<String>) {
    runApplication<SiuBackendApplication>(*args)
}
