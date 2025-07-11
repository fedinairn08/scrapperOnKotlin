package org.scrapper.integration

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.scrapper.environment.IntegrationEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.SQLException

@SpringBootTest
@Testcontainers
class IntegrationTests: IntegrationEnvironment() {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    companion object {
        private val container = IntegrationEnvironment.POSTGRE_SQL_CONTAINER

        @JvmStatic
        @DynamicPropertySource
        fun postgresqlProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { container.jdbcUrl }
            registry.add("spring.datasource.username") { container.username }
            registry.add("spring.datasource.password") { container.password }
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
        }
    }

    @Test
    fun postgresqlTest() {
        try {
            container.createConnection("").use { conn ->
                val stmt = conn.createStatement()
                val rs = stmt!!.executeQuery(
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'"
                )
                Assertions.assertThat(rs!!.next()).isTrue()
                Assertions.assertThat(rs.getInt(1)).isGreaterThan(0)
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }

        val id = 1L
        val chatId = 123L
        jdbcTemplate.update("INSERT INTO chat VALUES (?, ?)", id, chatId)

        val result = jdbcTemplate.queryForList("SELECT * FROM chat")
        Assertions.assertThat(result).isNotEmpty()
        Assertions.assertThat(result[0]!!["id"]).isEqualTo(id)
        Assertions.assertThat(result[0]!!["chat_id"]).isEqualTo(chatId)
    }
}
