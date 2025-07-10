package org.scrapper.configuration

import org.scrapper.dto.Scheduler
import org.scrapper.enums.AccessType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
data class ApplicationConfig(

        var scheduler: Scheduler,

        var databaseAccessType: AccessType,

        var useQueue: Boolean
)
