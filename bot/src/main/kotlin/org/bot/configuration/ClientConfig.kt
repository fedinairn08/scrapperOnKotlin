package org.bot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "client", ignoreUnknownFields = false)
data class ClientConfig(var scrapperBaseUrl: String)
