package org.scrapper.repository.jdbcImpl

import lombok.RequiredArgsConstructor
import org.scrapper.entity.Chat
import org.scrapper.entity.Link
import org.scrapper.repository.ChatRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.net.URI
import java.sql.Connection
import java.sql.ResultSet
import java.util.*

@RequiredArgsConstructor
@Repository
class ChatRepositoryJdbcImpl(private val jdbcTemplate: JdbcTemplate): ChatRepository {

    override fun save(chat: Chat): Chat {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection: Connection ->
            val ps = connection.prepareStatement(
                "INSERT INTO chat (chat_id) VALUES (?)",
                arrayOf("id")
            )
            ps.setLong(1, chat.chatId ?: throw IllegalArgumentException("ChatId must not be null"))
            ps
        }, keyHolder)

        chat.id = keyHolder.key?.toLong()
        return chat
    }

    override fun remove(chatId: Long) {
        jdbcTemplate.update("DELETE FROM chat WHERE id = ?", chatId)
    }

    override fun findAll(): List<Chat>? {
        val sql = """
            SELECT c.id AS chat_id, c.chat_id as chat_tg_id, l.id as link_id, l.url, l.last_update
            FROM chat AS c LEFT JOIN link AS l ON c.id = l.chat
        """.trimIndent()

        return jdbcTemplate.query(sql, this::mapRowToChatWithLinks)
    }

    override fun findByChatId(tgChatId: Long): Optional<Chat> {
        val sql = """
            SELECT c.id AS chat_id, c.chat_id as chat_tg_id, l.id as link_id, l.url, l.last_update
            FROM chat as c LEFT JOIN link as l ON c.id = l.chat WHERE c.chat_id = ?
        """.trimIndent()

        return try {
            val result = jdbcTemplate.query(sql, ResultSetExtractor { rs ->
                mapRowToChatWithLinks(rs).firstOrNull()
            }, tgChatId)
            Optional.ofNullable(result)
        } catch (e: EmptyResultDataAccessException) {
            Optional.empty()
        }
    }

    private fun mapRowToChatWithLinks(rs: ResultSet): List<Chat> {
        val chatMap = mutableMapOf<Long, Chat>()

        while (rs.next()) {
            var chatId = rs.getLong("chat_id")

            val chat = chatMap.computeIfAbsent(chatId) {
                Chat(
                    rs.getLong("chat_id"),
                    rs.getLong("chat_tg_id"),
                    mutableListOf())
            }

            val linkId = rs.getObject("link_id", Long::class.java)
            if (linkId != null) {
                try {
                    chat.links?.add(
                        Link(
                            linkId,
                            URI(rs.getString("url")),
                            rs.getTimestamp("last_update"))
                    )
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }

            chatMap[chatId] = chat
        }

        return chatMap.values.toList()
    }
}