package org.scrapper.service

import lombok.RequiredArgsConstructor
import org.scrapper.client.BotClient
import org.scrapper.configuration.ApplicationConfig
import org.scrapper.dto.request.LinkUpdateRequest
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class LinkUpdateService(
    private val botClient: BotClient,
    private val applicationConfig: ApplicationConfig,
    private val scrapperQueueProducer: ScrapperQueueProducer) {

    fun sendLinkUpdate(updateRequest: LinkUpdateRequest) {
        if (applicationConfig.useQueue) {
            scrapperQueueProducer.send(updateRequest)
        } else {
            botClient.updateLink(updateRequest)
        }
    }
}