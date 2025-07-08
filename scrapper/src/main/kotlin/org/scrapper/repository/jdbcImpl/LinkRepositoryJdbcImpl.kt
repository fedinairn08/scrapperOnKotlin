package org.scrapper.repository.jdbcImpl

import lombok.RequiredArgsConstructor
import org.scrapper.entity.Chat
import org.scrapper.entity.Link
import org.scrapper.repository.LinkRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.URISyntaxException
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp

@Repository
@RequiredArgsConstructor
class LinkRepositoryJdbcImpl(private val jdbcTemplate: JdbcTemplate): LinkRepository{
    override fun save(link: Link): Link {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection: Connection ->
            val ps = connection.prepareStatement(
                "INSERT INTO link (url, chat, last_update) VALUES (?, ?, ?)",
                arrayOf("id")
            )
            ps.setString(1, link.url.toString())
            ps.setObject(2, link.chat?.id)
            ps.setTimestamp(3, link.lastUpdate)
            ps
        }, keyHolder)

        link.id = keyHolder.key?.toLong()
        return link
    }

    override fun remove(linkId: Long) {
        jdbcTemplate.update("DELETE FROM link WHERE id = ?", linkId)
    }

    override fun findAll(): List<Link> {
        val sql = """
            SELECT c.id AS chat_id, c.chat_id as chat_tg_id, l.id as link_id, l.url, l.last_update
            FROM chat as c LEFT JOIN link as l ON c.id = l.chat
            """.trimMargin()

        return jdbcTemplate.query(sql, this::mapRowToLinkWithChat)
    }

    override fun updateLastUpdate(linkId: Long, timeUpdate: Timestamp) {
        val sql = "UPDATE link SET last_update = ? WHERE id = ?"
        jdbcTemplate.update(sql, timeUpdate, linkId)
    }

    override fun findAllOutdatedLinks(timestamp: Timestamp): List<Link> {
        val sql = """
            SELECT c.id AS chat_id, c.chat_id as chat_tg_id, l.id as link_id, l.url, l.last_update
            FROM chat as c 
            RIGHT JOIN link as l ON c.id = l.chat 
            WHERE l.last_update < ?
        """.trimIndent()

        return jdbcTemplate.query(sql, this::mapRowToLinkWithChat, timestamp)
    }

    override fun deleteAllByChat_ChatId(chatId: Long) {
        jdbcTemplate.update("DELETE FROM link WHERE chat_id = ?", chatId)
    }

    @Throws(SQLException::class)
    private fun mapRowToLinkWithChat(rs: ResultSet, rowNum: Int): Link {
        try {
            val rawUrl = rs.getString("url")
            val cleanedUrl = rawUrl?.replace("\"".toRegex(), "") ?: ""

            val chatId = rs.getObject("chat_id", Long::class.java)
            val chatTgId = rs.getObject("chat_tg_id", Long::class.java)

            val chat = if (chatId != null && chatTgId != null) {
                Chat(chatId, chatTgId, mutableListOf())
            } else {
                null
            }

            return Link(
                rs.getObject("link_id", Long::class.java),
                URI(cleanedUrl),
                chat,
                rs.getTimestamp("last_update")
            )
        } catch (e: URISyntaxException) {
            throw RuntimeException("Invalid URL format in database: ${rs.getString("url")}", e)
        }
    }
}