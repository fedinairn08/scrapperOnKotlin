package org.scrapper.mapper

import org.mapstruct.Mapper
import org.scrapper.dto.response.LinkResponse
import org.scrapper.dto.response.ListLinksResponse
import org.scrapper.entity.Link

@Mapper(componentModel = "spring")
interface LinkMapper {

    fun toLinkResponse(link: Link): LinkResponse

    fun toListLinksResponse(links: List<Link>): ListLinksResponse {

        val responses = links.map { toLinkResponse(it) }

        return ListLinksResponse(responses, responses.size)
    }
}