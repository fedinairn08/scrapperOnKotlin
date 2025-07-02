package org.scrapper.dto.stackoverflow

import java.time.OffsetDateTime

data class StackOverflowQuestion(
        var isAnswered: Boolean,
        var lastActivityDate: OffsetDateTime,
        var creationDate: OffsetDateTime,
        var lastEditDate: OffsetDateTime,
        var questionId: Long
)
