package org.scrapper.dto.response

import java.time.OffsetDateTime

data class GitHubRepositoryResponse(
        var id: Long,
        var nodeId: String,
        var name: String,
        var fullName: String,
        var htmlUrl: String,
        var description: String,
        var pushedAt: OffsetDateTime,
        var createdAt: OffsetDateTime,
        var updatedAt: OffsetDateTime
)
