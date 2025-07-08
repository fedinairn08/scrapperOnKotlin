package org.scrapper.dto.request

import java.net.URI

data class LinkUpdateRequest(

    var id: Long,

    var url: URI,

    var description: String,

    var tgChatIds: List<Long>
)
