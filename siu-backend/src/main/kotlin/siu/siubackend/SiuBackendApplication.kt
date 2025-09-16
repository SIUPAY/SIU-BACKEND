package siu.siubackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SiuBackendApplication

fun main(args: Array<String>) {
    runApplication<SiuBackendApplication>(*args)
}
