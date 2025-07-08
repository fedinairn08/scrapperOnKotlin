package org.scrapper.service

import lombok.RequiredArgsConstructor
import org.linkparser.model.LinkParserResult
import org.linkparser.service.LinkParser
import org.scrapper.dto.request.LinkUpdateRequest
import org.scrapper.entity.Link
import org.scrapper.service.api.ApiService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
@RequiredArgsConstructor
@ComponentScan("org.linkparser.service")
class LinkUpdaterScheduler(
    private val linkService: LinkService,

    private val linkParser: LinkParser,

    private val apiService: ApiService,

    private val linkUpdateService: LinkUpdateService,

    @Value("\${scheduler.update}")
    private var timeLinkUpdate: Int
) {

    @Scheduled(fixedDelayString = "#{@scheduler.interval().toMillis()}")
    fun update() {
        val links: List<Link> = linkService.findAllOutdatedLinks(Timestamp(System.currentTimeMillis() - timeLinkUpdate))
        updateLinks(links)
    }

    fun updateLinks(links: List<Link>) {
        for (link in links) {
            val linkParserResult: LinkParserResult = link.url?.let { linkParser.parseUrl(it) }!!
            val description: String = apiService.checkUpdate(linkParserResult, link)
            linkUpdateService.sendLinkUpdate(
                LinkUpdateRequest(
                    link.id!!,
                    link.url!!,
                    description,
                    listOf(link.chat!!.chatId!!)
                )
            )
            linkService.updateLastUpdate(link.id!!, Timestamp(System.currentTimeMillis()))
        }
    }
}