package org.scrapper.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class LinkUpdaterScheduler {

    @Scheduled(fixedDelayString = "#{@scheduler.interval().toMillis()}")
    fun update() {

    }
}