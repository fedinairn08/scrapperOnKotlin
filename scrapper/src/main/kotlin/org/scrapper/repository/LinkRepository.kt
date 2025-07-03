package org.scrapper.repository

import org.scrapper.entity.Link
import java.sql.Timestamp

interface LinkRepository {

    fun save(link: Link): Link

    fun remove(linkId: Long)

    fun findAll(): List<Link>

    fun updateLastUpdate(linkId: Long, timeUpdate: Timestamp)

    fun findAllOutdatedLinks(timestamp: Timestamp): List<Link>

    fun deleteAllByChat_ChatId(chatId: Long)
}