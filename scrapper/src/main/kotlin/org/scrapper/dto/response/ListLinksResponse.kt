package org.scrapper.dto.response

data class ListLinksResponse(
        var links: List<LinkResponse>,
        var size: Int
)
