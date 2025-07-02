package org.scrapper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ScrapperApplication

fun main(args: Array<String>) {
    runApplication<ScrapperApplication>(*args)
}
