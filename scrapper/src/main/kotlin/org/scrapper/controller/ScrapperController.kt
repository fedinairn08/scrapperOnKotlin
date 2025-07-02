package org.scrapper.controller

import lombok.RequiredArgsConstructor
import org.scrapper.dto.request.AddLinkRequest
import org.scrapper.dto.response.LinkResponse
import org.scrapper.dto.response.ListLinksResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequiredArgsConstructor
class ScrapperController {

    @PostMapping("/tg-chat/{id}")
    fun registerChat(@PathVariable id: Long): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/tg-chat/{id}")
    fun deleteChat(@PathVariable id: Long): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/links")
    fun getLinks(@RequestHeader("Tg-Chat-Id") tgChatId: Long): ResponseEntity<ListLinksResponse> {
        return ResponseEntity.ok().build()
    }

    @PostMapping("/links")
    fun addLink(@RequestHeader("Tg-Chat-Id") tgChatId: Long,
                @RequestBody request: AddLinkRequest): ResponseEntity<LinkResponse> {
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/links/delete")
    fun removeLink(@RequestHeader("Tg-Chat-Id") tgChatId: Long,
                   @RequestParam("url") url: URI): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }
}