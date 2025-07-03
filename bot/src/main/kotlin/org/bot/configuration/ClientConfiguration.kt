package org.bot.configuration

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

@Configuration
@RequiredArgsConstructor
class ClientConfiguration(private val clientConfig: ClientConfig) {

    @Bean("scrapperRestClient")
    fun scrapperClient(): RestClient {
        return RestClient.builder()
                .baseUrl(clientConfig.scrapperBaseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build()
    }
}