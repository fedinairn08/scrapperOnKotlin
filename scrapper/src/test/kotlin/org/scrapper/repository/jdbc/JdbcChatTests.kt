package org.scrapper.repository.jdbc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.scrapper.entity.Chat
import org.scrapper.environment.IntegrationEnvironment
import org.scrapper.repository.ChatRepository
import org.scrapper.repository.jdbcImpl.ChatRepositoryJdbcImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(
    classes = [IntegrationEnvironment.IntegrationEnvironmentConfiguration::class, ChatRepositoryJdbcImpl::class
    ]
)
class JdbcChatTests: IntegrationEnvironment() {

    companion object {
        private lateinit var testChat: Chat

        @Autowired
        private lateinit var jdbcTemplate: JdbcTemplate

        @JvmStatic
        @BeforeAll
        fun setTestChat(): Unit {
            testChat = Chat()
            testChat.chatId = 1L
        }

        @BeforeEach
        fun cleanDatabase() {
            jdbcTemplate.update("DELETE FROM link")
            jdbcTemplate.update("DELETE FROM chat")
        }
    }

    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Test
    @Transactional
    @Rollback
    fun saveTest() {
        val chat = chatRepository.save(testChat)

        assertThat(chat.chatId).isEqualTo(testChat.chatId)
    }

    @Test
    @Transactional
    @Rollback
    fun removeTest() {
        val chat = chatRepository.save(testChat)
        chat.id?.let { chatRepository.remove(it) }

        assertThat(chatRepository.findAll()).hasSize(0)
    }
}