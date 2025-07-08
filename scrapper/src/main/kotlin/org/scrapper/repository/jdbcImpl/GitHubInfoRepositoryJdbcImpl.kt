package org.scrapper.repository.jdbcImpl

import lombok.RequiredArgsConstructor
import org.scrapper.entity.Chat
import org.scrapper.entity.GitHubInfo
import org.scrapper.entity.Link
import org.scrapper.repository.GitHubInfoRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import java.net.URI
import java.sql.Connection
import java.sql.ResultSet

@RequiredArgsConstructor
class GitHubInfoRepositoryJdbcImpl(private val jdbcTemplate: JdbcTemplate): GitHubInfoRepository {
    override fun save(gitHubInfo: GitHubInfo) {
        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection: Connection ->
            val ps = connection.prepareStatement(
                "INSERT INTO gitHub_info (last_commit_count, last_branch_count, link_id) VALUES (?, ?, ?)",
                arrayOf("id")
            )
            ps.setInt(1, gitHubInfo.lastCommitCount)
            ps.setInt(2, gitHubInfo.lastBranchCount)
            ps.setObject(3, gitHubInfo.link?.id)
            ps
        }, keyHolder)

        gitHubInfo.id = keyHolder.key?.toLong()
    }

    override fun find(linkId: Long): GitHubInfo? {
        val sql = "SELECT * FROM github_info WHERE link_id = ?"

        return try {
            jdbcTemplate.queryForObject(sql, this::mapRowToGitHubInfo, linkId)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    override fun update(id: Long, lastCommitCount: Int, lastBranchCount: Int) {
        val sql = "UPDATE github_info SET last_commit_count = ?, last_branch_count = ? WHERE id = ?"
        jdbcTemplate.update(sql, lastCommitCount, lastBranchCount, id)
    }

    private fun mapRowToGitHubInfo(rs: ResultSet, rowNum: Int): GitHubInfo {
        val id = rs.getObject("id", Long::class.java)
        val lastCommitCount = rs.getObject("last_commit_count", Int::class.javaObjectType)
        val lastBranchCount = rs.getObject("last_branch_count", Int::class.javaObjectType)
        val linkId = rs.getObject("link_id", Long::class.java)

        val link = jdbcTemplate.queryForObject(
            "SELECT c.id AS chat_id, c.chat_id as chat_tg_id, l.id as link_id, l.url, l.last_update " +
                    "FROM chat as c RIGHT JOIN link as l ON c.id = l.chat WHERE l.id = ?",
            this::mapRowToLinkWithChat,
            linkId
        )

        return GitHubInfo(id, lastCommitCount, lastBranchCount, link!!)
    }

    private fun mapRowToLinkWithChat(rs: ResultSet, rowNum: Int): Link {
        return try {
            val rawUrl = rs.getString("url")
            val cleanedUrl = rawUrl?.replace("^\"|\"\$".toRegex(), "") ?: ""

            var chatId = rs.getObject("chat_id", Long::class.java)
            val chatTgId = rs.getObject("chat_tg_id", Long::class.java)

            var chat = if (chatId != null && chatTgId != null) {
                Chat(
                    chatId,
                    chatTgId,
                    mutableListOf())
            } else {
                null
            }

            return Link(
                rs.getObject("link_id", Long::class.java),
                URI(cleanedUrl),
                chat,
                rs.getTimestamp("last_update"))
        } catch (e: Exception) {
            throw RuntimeException("Invalid URL format in database: ${rs.getString("url")}", e)
        }
    }
}