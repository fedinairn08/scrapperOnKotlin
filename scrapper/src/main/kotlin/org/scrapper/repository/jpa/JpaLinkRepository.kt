package org.scrapper.repository.jpa

import org.scrapper.entity.Link
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp

interface JpaLinkRepository: JpaRepository<Link, Long> {
    fun findAllByLastUpdateLessThan(timestamp: Timestamp): List<Link>

    fun deleteAllByChat_ChatId(chatId: Long)
}