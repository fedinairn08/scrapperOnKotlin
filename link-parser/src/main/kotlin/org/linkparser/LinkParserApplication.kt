package org.linkparser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LinkParserApplication

fun main(args: Array<String>) {
    runApplication<LinkParserApplication>(*args)
}
