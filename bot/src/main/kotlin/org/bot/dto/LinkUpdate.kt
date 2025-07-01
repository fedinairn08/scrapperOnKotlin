package org.bot.dto

import java.net.URI

data class LinkUpdate(
        var id: Long,
        var url: URI,
        var description: String,
        var tgChatIds: List<Long>
)
