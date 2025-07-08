package org.scrapper.service

import org.scrapper.entity.Link
import java.net.URI
import java.sql.Timestamp

interface LinkService {

    fun add(tgChatId: Long, url: URI): Link

    fun remove(tgChatId: Long, url: URI)

    fun listAll(tgChatId: Long): List<Link>

    fun updateLastUpdate(linkId: Long, timeUpdate: Timestamp)

    fun findAllOutdatedLinks(timestamp: Timestamp): List<Link>

    fun deleteAllByChat_ChatId(chatId: Long?)
}