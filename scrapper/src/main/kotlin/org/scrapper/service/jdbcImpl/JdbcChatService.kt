package org.scrapper.service.jdbcImpl

import lombok.RequiredArgsConstructor
import org.scrapper.entity.Chat
import org.scrapper.repository.ChatRepository
import org.scrapper.service.ChatService
import java.util.*

@RequiredArgsConstructor
class JdbcChatService(private val chatRepository: ChatRepository): ChatService {
    override fun register(chatId: Long) {
        val chat = Chat()
        chat.chatId = chatId
        chatRepository.save(chat)
    }

    override fun unregister(chatId: Long) {
        chatRepository.findByChatId(chatId).get().id?.let { chatRepository.remove(it) }
    }

    override fun getByChatId(tgChatId: Long): Optional<Chat> {
        return chatRepository.findByChatId(tgChatId)
    }
}