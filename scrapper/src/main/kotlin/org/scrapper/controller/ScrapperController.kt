package org.scrapper.controller

import lombok.RequiredArgsConstructor
import org.scrapper.dto.request.AddLinkRequest
import org.scrapper.dto.response.LinkResponse
import org.scrapper.dto.response.ListLinksResponse
import org.scrapper.entity.Link
import org.scrapper.mapper.LinkMapper
import org.scrapper.service.ChatService
import org.scrapper.service.LinkService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequiredArgsConstructor
class ScrapperController(
    private val chatService: ChatService,
    private val linkService: LinkService,
    private val linkMapper: LinkMapper) {

    @PostMapping("/tg-chat/{id}")
    fun registerChat(@PathVariable id: Long): ResponseEntity<Void> {
        chatService.register(id)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/tg-chat/{id}")
    fun deleteChat(@PathVariable id: Long): ResponseEntity<Void> {
        chatService.unregister(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/links")
    fun getLinks(@RequestHeader("Tg-Chat-Id") tgChatId: Long): ResponseEntity<ListLinksResponse> {
        val links: List<Link> = linkService.listAll(tgChatId)
        return ResponseEntity.ok(linkMapper.convertListLinkToListLinksResponse(links))
    }

    @PostMapping("/links")
    fun addLink(@RequestHeader("Tg-Chat-Id") tgChatId: Long,
                @RequestBody request: AddLinkRequest): ResponseEntity<LinkResponse> {
        val link = linkService.add(tgChatId, request.link)
        return ResponseEntity.ok(linkMapper.convertLinkToLinkResponse(link))
    }

    @DeleteMapping("/links/delete")
    fun removeLink(@RequestHeader("Tg-Chat-Id") tgChatId: Long,
                   @RequestParam("url") url: URI): ResponseEntity<Void> {
        linkService.remove(tgChatId, url)
        return ResponseEntity.ok().build()
    }
}