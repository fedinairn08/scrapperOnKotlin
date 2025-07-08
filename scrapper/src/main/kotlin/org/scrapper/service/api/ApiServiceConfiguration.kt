package org.scrapper.service.api

import lombok.RequiredArgsConstructor
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@RequiredArgsConstructor
class ApiServiceConfiguration(private val context: ApplicationContext) {

    @Bean
    fun apiService(): ApiService {
        val services = ArrayList(Reflections(ClasspathHelper.forClass(ApiService::class.java))
                .getSubTypesOf(ApiService::class.java))

        val firstService: ApiService = context.getBean(services.removeFirst())
        var currentService = firstService

        for (service in services) {
            currentService = currentService.setNextService(context.getBean(service))
        }
        return firstService
    }
}