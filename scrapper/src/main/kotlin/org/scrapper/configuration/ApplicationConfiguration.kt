package org.scrapper.configuration

import org.scrapper.dto.Scheduler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import java.time.Duration
import javax.sql.DataSource

@Configuration
class ApplicationConfiguration {

    @Bean
    fun scheduler(@Value("\${app.scheduler.interval}") interval: Duration): Scheduler {
        return Scheduler(interval)
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }
}