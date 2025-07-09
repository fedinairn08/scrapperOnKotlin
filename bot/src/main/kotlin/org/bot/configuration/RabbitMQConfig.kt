package org.bot.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "rabbit", ignoreUnknownFields = false)
data class RabbitMQConfig(
    var queue: String,

    var exchange: String
)
