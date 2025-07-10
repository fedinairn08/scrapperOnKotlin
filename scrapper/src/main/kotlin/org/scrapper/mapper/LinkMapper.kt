package org.scrapper.mapper

import org.scrapper.dto.response.LinkResponse
import org.scrapper.dto.response.ListLinksResponse
import org.scrapper.entity.Link
import org.springframework.stereotype.Component


@Component
class LinkMapper {

    fun convertLinkToLinkResponse(link: Link): LinkResponse {
        return LinkResponse(link.id!!, link.url!!)
    }

    fun convertListLinkToListLinksResponse(links: List<Link>): ListLinksResponse {
        return ListLinksResponse(
            links.stream()
                .map(this::convertLinkToLinkResponse)
                .toList(),
            links.size
        )
    }
}