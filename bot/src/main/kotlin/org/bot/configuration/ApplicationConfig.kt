package org.bot.configuration

import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
data class ApplicationConfig(

        @field:NotNull
        var test: String,

        @field:NotNull
        var token: String
)
