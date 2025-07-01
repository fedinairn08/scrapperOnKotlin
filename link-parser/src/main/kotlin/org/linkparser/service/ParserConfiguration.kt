package org.linkparser.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ParserConfiguration {

    @Bean
    fun linkParser(): LinkParser {
        val linkParser = LinkParserStackOverflow()
        linkParser.nextParser = LinkParserGitHub()
        return linkParser
    }
}