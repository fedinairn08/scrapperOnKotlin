package org.scrapper.client

import lombok.RequiredArgsConstructor
import org.scrapper.dto.response.StackOverflowQuestionResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
@RequiredArgsConstructor
class StackOverflowClient(private val stackOverflowRestClient: RestClient) {

    fun getQuestionInfo(id: Long): StackOverflowQuestionResponse? {
        return stackOverflowRestClient.get()
                .uri("/questions/{id}?site=stackoverflow", id)
                .retrieve()
                .body(StackOverflowQuestionResponse::class.java)
    }
}