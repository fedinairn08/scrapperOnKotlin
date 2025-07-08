package org.scrapper.configuration.access

import org.scrapper.repository.jpa.JpaChatRepository
import org.scrapper.repository.jpa.JpaGitHubInfoRepository
import org.scrapper.repository.jpa.JpaLinkRepository
import org.scrapper.service.ChatService
import org.scrapper.service.GitHubInfoService
import org.scrapper.service.LinkService
import org.scrapper.service.jpaImpl.JpaChatService
import org.scrapper.service.jpaImpl.JpaGitHubInfoService
import org.scrapper.service.jpaImpl.JpaLinkService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "app", name = ["database-access-type"], havingValue = "jpa")
class JpaAccessConfiguration {

    @Bean
    fun chatService(jpaChatRepository: JpaChatRepository?, linkService: LinkService?): ChatService {
        return JpaChatService(jpaChatRepository!!, linkService!!)
    }

    @Bean
    fun linkService(
        jpaLinkRepository: JpaLinkRepository?,
        jpaGitHubInfoRepository: JpaGitHubInfoRepository?,
        jpaChatRepository: JpaChatRepository?
    ): LinkService {
        return JpaLinkService(jpaLinkRepository!!, jpaGitHubInfoRepository!!, jpaChatRepository!!)
    }

    @Bean
    fun gitHubInfoService(jpaGitHubInfoRepository: JpaGitHubInfoRepository?): GitHubInfoService {
        return JpaGitHubInfoService(jpaGitHubInfoRepository!!)
    }
}