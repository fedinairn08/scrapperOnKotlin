package org.scrapper.service.jpaImpl

import lombok.RequiredArgsConstructor
import org.scrapper.entity.Chat
import org.scrapper.entity.Link
import org.scrapper.repository.jpa.JpaChatRepository
import org.scrapper.service.ChatService
import org.scrapper.service.LinkService
import org.springframework.transaction.annotation.Transactional
import java.util.*

@RequiredArgsConstructor
open class JpaChatService(
    private val jpaChatRepository: JpaChatRepository,
    private val linkService: LinkService
    ): ChatService {
    override fun register(chatId: Long) {
        val chat = Chat()
        chat.chatId = chatId
        jpaChatRepository.save(chat)
    }

    @Transactional
    override fun unregister(chatId: Long) {
        val chat = jpaChatRepository.findByChatId(chatId)

        val linksToRemove: List<Link> = ArrayList(chat.links!!)

        for (link in linksToRemove) {
            linkService.remove(chatId, link.url!!)
        }

        jpaChatRepository.delete(chat)
    }

    override fun getByChatId(tgChatId: Long): Optional<Chat> {
        return Optional.ofNullable(jpaChatRepository.findByChatId(tgChatId))
    }
}