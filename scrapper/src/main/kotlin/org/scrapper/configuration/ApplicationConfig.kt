package org.scrapper.configuration

import jakarta.validation.constraints.NotNull
import org.scrapper.dto.Scheduler
import org.scrapper.enums.AccessType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
data class ApplicationConfig(

        @field:NotNull
        var test: String,

        var scheduler: Scheduler,

        var databaseAccessType: AccessType,

        var useQueue: Boolean
)
