package org.scrapper.environment

import liquibase.Contexts
import liquibase.LabelExpression
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.DirectoryResourceAccessor
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.PlatformTransactionManager
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.io.File
import java.sql.DriverManager
import javax.sql.DataSource

@ContextConfiguration(classes = [IntegrationEnvironment.IntegrationEnvironmentConfiguration::class])
abstract class IntegrationEnvironment {

    companion object {
        private const val DB_NAME = "scrapper"
        private const val DB_USER = "root"
        private const val DB_PASSWORD = "root"
        private val TEST_DATA_SOURCE: DataSource

        val POSTGRE_SQL_CONTAINER: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>(
            DockerImageName.parse("postgres:latest")
        ).apply {
            withDatabaseName(DB_NAME)
            withUsername(DB_USER)
            withPassword(DB_PASSWORD)
        }

        private fun applyMigrations() {
            try {
                DriverManager.getConnection(
                    POSTGRE_SQL_CONTAINER.jdbcUrl,
                    POSTGRE_SQL_CONTAINER.username,
                    POSTGRE_SQL_CONTAINER.password
                ).use { connection ->
                    val changeLogPath = File(".").toPath().toAbsolutePath().parent.parent
                        .resolve("migrations")
                    val liquibase = Liquibase(
                        "master.xml",
                        DirectoryResourceAccessor(changeLogPath),
                        DatabaseFactory.getInstance()
                            .findCorrectDatabaseImplementation(JdbcConnection(connection))
                    )
                    liquibase.update(Contexts(), LabelExpression())
                }
            } catch (e: Exception) {
                throw RuntimeException("Не удалось запустить миграции Liquibase", e)
            }
        }

        @DynamicPropertySource
        fun jdbcProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { POSTGRE_SQL_CONTAINER.jdbcUrl }
            registry.add("spring.datasource.username") { POSTGRE_SQL_CONTAINER.username }
            registry.add("spring.datasource.password") { POSTGRE_SQL_CONTAINER.password }
        }

        init {
            POSTGRE_SQL_CONTAINER.start()
            applyMigrations()
            TEST_DATA_SOURCE = DataSourceBuilder.create()
                .url(POSTGRE_SQL_CONTAINER.jdbcUrl)
                .username(POSTGRE_SQL_CONTAINER.username)
                .password(POSTGRE_SQL_CONTAINER.password)
                .build()
        }
    }

    @Configuration
    class IntegrationEnvironmentConfiguration {
        @Bean
        fun dataSource(): DataSource {
            return TEST_DATA_SOURCE
        }

        @Bean
        fun jdbcTemplate(dataSource: DataSource?): JdbcTemplate {
            return JdbcTemplate(dataSource!!)
        }

        @Bean
        fun transactionManager(dataSource: DataSource?): PlatformTransactionManager {
            return JdbcTransactionManager(dataSource!!)
        }
    }
}