package org.bot.configuration.access

import org.scrapper.repository.ChatRepository
import org.scrapper.repository.GitHubInfoRepository
import org.scrapper.repository.LinkRepository
import org.scrapper.repository.jdbcImpl.ChatRepositoryJdbcImpl
import org.scrapper.repository.jdbcImpl.GitHubInfoRepositoryJdbcImpl
import org.scrapper.repository.jdbcImpl.LinkRepositoryJdbcImpl
import org.scrapper.service.ChatService
import org.scrapper.service.GitHubInfoService
import org.scrapper.service.LinkService
import org.scrapper.service.jdbcImpl.JdbcChatService
import org.scrapper.service.jdbcImpl.JdbcGitHubInfoService
import org.scrapper.service.jdbcImpl.JdbcLinkService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
@ConditionalOnProperty(prefix = "app", name = ["database-access-type"], havingValue = "jdbc")
class JdbcAccessConfiguration {

    @Bean
    fun linkRepository(jdbcTemplate: JdbcTemplate): LinkRepository {
        return LinkRepositoryJdbcImpl(jdbcTemplate)
    }

    @Bean
    fun chatRepository(jdbcTemplate: JdbcTemplate): ChatRepository {
        return ChatRepositoryJdbcImpl(jdbcTemplate)
    }

    @Bean
    fun gitHubInfoRepository(jdbcTemplate: JdbcTemplate): GitHubInfoRepository {
        return GitHubInfoRepositoryJdbcImpl(jdbcTemplate)
    }

    @Bean
    fun chatService(chatRepository: ChatRepository): ChatService {
        return JdbcChatService(chatRepository)
    }

    @Bean
    fun linkService(
        linkRepository: LinkRepository,
        chatService: ChatService
    ): LinkService {
        return JdbcLinkService(linkRepository, chatService)
    }

    @Bean
    fun gitHubInfoService(gitHubInfoRepository: GitHubInfoRepository): GitHubInfoService {
        return JdbcGitHubInfoService(gitHubInfoRepository)
    }
}