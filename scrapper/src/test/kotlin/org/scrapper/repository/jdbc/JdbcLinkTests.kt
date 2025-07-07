package org.scrapper.repository.jdbc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import java.net.URI
import java.net.URISyntaxException
import java.sql.Timestamp

@SpringBootTest(
    classes = [IntegrationEnvironment.IntegrationEnvironmentConfiguration::class, LinkRepositoryJdbcImpl::class, ChatRepositoryJdbcImpl::class
    ]
)
class JdbcLinkTests: IntegrationEnvironment() {

    private var testLink: Link? = null

    @Autowired
    private var linkRepository: LinkRepository? = null

    @Autowired
    private var chatRepository: ChatRepository? = null

    @BeforeEach
    @Throws(URISyntaxException::class)
    fun setTestLink() {
        var chat = Chat()
        chat.chatId = 1L
        val savedChat = chatRepository?.save(chat)

        testLink = Link().apply {
            url = URI("http://localhost:8080")
            if (savedChat != null) {
                chat = savedChat
            }
            lastUpdate = Timestamp(System.currentTimeMillis())
        }
    }

    @Test
    @Transactional
    @Rollback
    fun saveTest() {
        val link: Link? = testLink?.let { linkRepository!!.save(it) }

        Assertions.assertNotNull(link)
        assertThat(link!!.url).isEqualTo(testLink!!.url)
    }
}