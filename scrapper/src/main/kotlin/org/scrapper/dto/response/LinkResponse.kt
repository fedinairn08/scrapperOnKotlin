package org.scrapper.dto.response

import java.net.URI

data class LinkResponse(
        var id: Long,
        var url: URI
)
