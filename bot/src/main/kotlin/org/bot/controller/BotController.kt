package org.bot.controller

import lombok.RequiredArgsConstructor
import org.bot.dto.LinkUpdate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class BotController {

    @PostMapping("/updates")
    fun sendUpdate(@RequestBody linkUpdate: LinkUpdate): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }
}