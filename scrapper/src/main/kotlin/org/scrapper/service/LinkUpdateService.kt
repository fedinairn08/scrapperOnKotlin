package org.scrapper.service

import lombok.RequiredArgsConstructor
import org.scrapper.client.BotClient
import org.scrapper.dto.request.LinkUpdateRequest
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class LinkUpdateService(private val botClient: BotClient) {

    fun sendLinkUpdate(updateRequest: LinkUpdateRequest) {
        botClient.updateLink(updateRequest)
    }
}