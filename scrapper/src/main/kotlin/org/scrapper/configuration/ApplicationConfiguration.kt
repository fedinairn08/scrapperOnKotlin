package org.scrapper.configuration

import org.scrapper.dto.Scheduler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class ApplicationConfiguration {

    @Bean
    fun scheduler(@Value("\${app.scheduler.interval}") interval: Duration): Scheduler {
        return Scheduler(interval)
    }
}