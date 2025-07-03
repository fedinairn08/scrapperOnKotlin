package org.bot.client

import lombok.RequiredArgsConstructor
import org.bot.dto.scrapper.request.AddLinkRequest
import org.bot.dto.scrapper.response.LinkResponse
import org.bot.dto.scrapper.response.ListLinksResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriBuilder
import java.net.URI
import java.util.*

@Component
@RequiredArgsConstructor
class ScrapperClient(private val scrapperRestClient: RestClient) {

    fun registerChat(chatId: Long) {
        scrapperRestClient.post()
                .uri("/tg-chat/$chatId")
                .retrieve()
                .toBodilessEntity()
    }

    fun getLinks(chatId: Long): Optional<ListLinksResponse> {
        return Optional.ofNullable(scrapperRestClient.get()
                .uri("/links")
                .header("Tg-Chat-Id", chatId.toString())
                .retrieve()
                .toEntity(ListLinksResponse::class.java)
                .body)
    }

    fun addLink(chatId: Long, link: AddLinkRequest): Optional<LinkResponse> {
        return Optional.ofNullable(scrapperRestClient.post()
                .uri("/links")
                .header("Tg-Chat-Id", chatId.toString())
                .body(link)
                .retrieve()
                .toEntity(LinkResponse::class.java)
                .body)
    }

    fun removeLink(chatId: Long, uri: URI): Boolean {
        return scrapperRestClient.delete()
                .uri { uriBuilder: UriBuilder ->
                    uriBuilder
                            .path("/links/delete")
                            .queryParam("url", uri)
                            .build()
                }
                .header("Tg-Chat-Id", chatId.toString())
                .retrieve()
                .toBodilessEntity()
                .statusCode.is2xxSuccessful
    }
}