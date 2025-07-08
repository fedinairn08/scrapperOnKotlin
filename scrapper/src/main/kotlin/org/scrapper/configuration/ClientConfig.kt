package org.scrapper.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "client")
data class ClientConfig(
        val githubBaseUrl: String,

        val stackoverflowBaseUrl: String,

        val botBaseUrl: String
)
