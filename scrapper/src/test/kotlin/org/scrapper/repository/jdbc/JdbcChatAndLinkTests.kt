package org.scrapper.repository.jdbc

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.scrapper.entity.Chat
import org.scrapper.entity.Link
import org.scrapper.environment.IntegrationEnvironment
import org.scrapper.repository.ChatRepository
import org.scrapper.repository.LinkRepository
import org.scrapper.repository.jdbcImpl.ChatRepositoryJdbcImpl
import org.scrapper.repository.jdbcImpl.LinkRepositoryJdbcImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import java.net.URI
import java.net.URISyntaxException
import java.sql.Timestamp

@SpringBootTest(
    classes = [
        IntegrationEnvironment.IntegrationEnvironmentConfiguration::class,
        ChatRepositoryJdbcImpl::class,
        LinkRepositoryJdbcImpl::class
    ]
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcChatAndLinkTests {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Autowired
    private lateinit var linkRepository: LinkRepository

    companion object {

        private lateinit var testChat: Chat

        @JvmStatic
        @BeforeAll
        fun setTestData(): Unit {
            testChat = Chat()
            testChat.chatId = 1L

        }
    }

    @BeforeEach
    fun cleanDatabase() {
        jdbcTemplate.update("DELETE FROM link")
        jdbcTemplate.update("DELETE FROM chat")
    }

    @Test
    @Transactional
    @Rollback
    @Throws(URISyntaxException::class)
    fun cascadeDeleteChatAndLinkTest() {
        chatRepository.save(testChat)

        val link1: Link = Link().apply {
            url = URI("http://localhost:8080/1")
            chat = testChat
            lastUpdate = Timestamp(System.currentTimeMillis())
        }

        val link2: Link = Link().apply {
            url = URI("http://localhost:8080/2")
            chat = testChat
            lastUpdate = Timestamp(System.currentTimeMillis())
        }


        linkRepository.save(link1)
        linkRepository.save(link2)

        val savingChat = testChat.chatId?.let { chatRepository.findByChatId(it) }

        if (savingChat != null) {
            Assertions.assertTrue(savingChat.isPresent)
        }
        if (savingChat != null) {
            savingChat.get().links?.let { assertEquals(it.size, 2) }
        }

        if (savingChat != null) {
            savingChat.get().id?.let { chatRepository.remove(it) }
        }

        val links: List<Link> = linkRepository.findAll()

        Assertions.assertTrue(links.isEmpty())
    }
}