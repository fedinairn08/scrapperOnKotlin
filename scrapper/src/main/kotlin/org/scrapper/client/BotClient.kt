package org.scrapper.client

import lombok.RequiredArgsConstructor
import org.scrapper.dto.request.LinkUpdateRequest
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
@RequiredArgsConstructor
class BotClient(private val botRestClient: RestClient) {
    fun updateLink(linkUpdateRequest: LinkUpdateRequest) {
        botRestClient.post()
            .uri("/updates")
            .contentType(MediaType.APPLICATION_JSON)
            .body(linkUpdateRequest)
            .retrieve()
            .toBodilessEntity()
    }
}