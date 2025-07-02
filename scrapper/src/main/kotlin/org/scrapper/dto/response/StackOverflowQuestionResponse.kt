package org.scrapper.dto.response

import org.scrapper.dto.stackoverflow.StackOverflowQuestion

data class StackOverflowQuestionResponse(var items: List<StackOverflowQuestion>)
