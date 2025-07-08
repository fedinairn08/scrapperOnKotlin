package org.scrapper.service.jdbcImpl

import lombok.RequiredArgsConstructor
import org.scrapper.entity.Link
import org.scrapper.repository.LinkRepository
import org.scrapper.service.ChatService
import org.scrapper.service.LinkService
import java.net.URI
import java.sql.Timestamp

@RequiredArgsConstructor
class JdbcLinkService(private val linkRepository: LinkRepository, private val chatService: ChatService): LinkService {
    override fun add(tgChatId: Long, url: URI): Link {
        val chat = chatService.getByChatId(tgChatId).orElseThrow {
            IllegalArgumentException("Chat not found with id: $tgChatId")
        }

        return linkRepository.save(
            Link().apply {
                this.url = url
                this.chat = chat
                this.lastUpdate = Timestamp(System.currentTimeMillis())
            }
        )
    }

    override fun remove(tgChatId: Long, url: URI) {
        val chat = chatService.getByChatId(tgChatId)

        val link = chat.get().links
            ?.firstOrNull { it.url == url }
            ?: throw IllegalArgumentException("Link not found")

        linkRepository.remove(link.id!!)
    }

    override fun listAll(tgChatId: Long): List<Link> {
        val chat = chatService.getByChatId(tgChatId)

        val links = chat.get().links
        if (links.isNullOrEmpty()) {
            throw RuntimeException("Links not found")
        }

        return links
    }

    override fun updateLastUpdate(linkId: Long, timeUpdate: Timestamp) {
        linkRepository.updateLastUpdate(linkId, timeUpdate)
    }

    override fun findAllOutdatedLinks(timestamp: Timestamp): List<Link> {
        return linkRepository.findAllOutdatedLinks(timestamp)
    }

    override fun deleteAllByChat_ChatId(chatId: Long?) {
        linkRepository.deleteAllByChat_ChatId(chatId!!)
    }
}