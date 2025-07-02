package org.scrapper.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

@Configuration
class ClientConfiguration(private val clientConfig: ClientConfig) {

    @Bean("gitHubRestClient")
    fun gitHubClient(): RestClient {
        return RestClient.builder()
                .baseUrl(clientConfig.githubBaseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build()
    }

    @Bean("stackOverflowRestClient")
    fun stackOverflowClient(): RestClient {
        return RestClient.builder()
                .baseUrl(clientConfig.stackoverflowBaseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build()
    }
}