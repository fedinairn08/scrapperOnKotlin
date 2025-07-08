package org.scrapper.configuration

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

@Configuration
@RequiredArgsConstructor
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

    @Bean("botRestClient")
    fun botClient(): RestClient {
        return RestClient.builder()
            .baseUrl(clientConfig.botBaseUrl)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}