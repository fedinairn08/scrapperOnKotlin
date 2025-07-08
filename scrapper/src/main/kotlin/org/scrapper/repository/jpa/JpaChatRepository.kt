package org.scrapper.repository.jpa

import org.scrapper.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository

interface JpaChatRepository: JpaRepository<Chat, Long> {
    fun findByChatId(tgChatId: Long): Chat
}