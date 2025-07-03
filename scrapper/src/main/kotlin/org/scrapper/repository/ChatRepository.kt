package org.scrapper.repository

import org.scrapper.entity.Chat
import java.util.*

interface ChatRepository {
    fun save(chat: Chat): Chat

    fun remove(chatId: Long)

    fun findAll(): List<Chat>?

    fun findByChatId(tgChatId: Long): Optional<Chat>
}