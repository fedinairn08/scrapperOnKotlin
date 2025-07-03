package org.bot.dto.scrapper.response

data class ListLinksResponse(
        var links: List<LinkResponse>,
        var size: Int
)
