package org.scrapper.service.jpaImpl

import lombok.RequiredArgsConstructor
import org.scrapper.entity.Link
import org.scrapper.repository.jpa.JpaChatRepository
import org.scrapper.repository.jpa.JpaGitHubInfoRepository
import org.scrapper.repository.jpa.JpaLinkRepository
import org.scrapper.service.LinkService
import org.springframework.transaction.annotation.Transactional
import java.net.URI
import java.sql.Timestamp

@RequiredArgsConstructor
open class JpaLinkService(
    private val jpaLinkRepository: JpaLinkRepository,
    private val jpaGitHubInfoRepository: JpaGitHubInfoRepository,
    private val jpaChatRepository: JpaChatRepository
): LinkService {
    override fun add(tgChatId: Long, url: URI): Link {
        return jpaLinkRepository.save(
            Link().apply {
                chat = jpaChatRepository.findByChatId(tgChatId)
                this.url = url
                lastUpdate = Timestamp(System.currentTimeMillis())
            }
        )
    }

    @Transactional
    override fun remove(tgChatId: Long, url: URI) {
        val chat = jpaChatRepository.findByChatId(tgChatId)

        val linkToRemove: Link = chat.links
            ?.firstOrNull { it.url == url }
            ?: throw IllegalArgumentException("Link not found")

        jpaGitHubInfoRepository.deleteByLink(linkToRemove)

        chat.links!!.remove(linkToRemove)

        jpaLinkRepository.delete(linkToRemove)
    }

    override fun listAll(tgChatId: Long): List<Link> {
        return jpaChatRepository.findByChatId(tgChatId).links!!.toList()
    }

    override fun updateLastUpdate(linkId: Long, timeUpdate: Timestamp) {
        jpaLinkRepository.save(
            jpaLinkRepository.findById(linkId)
                .orElseThrow { RuntimeException("Link with id [$linkId] not found") }
                .apply {
                    lastUpdate = timeUpdate
                }
        )
    }

    override fun findAllOutdatedLinks(timestamp: Timestamp): List<Link> {
        return jpaLinkRepository.findAllByLastUpdateLessThan(timestamp)
    }

    override fun deleteAllByChat_ChatId(chatId: Long?) {
        jpaLinkRepository.deleteAllByChat_ChatId(chatId!!)
    }
}