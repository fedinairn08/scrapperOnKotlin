package org.linkparser.model

import java.net.URI

data class StackOverflowResult(var url: URI, var questionId: Long) : LinkParserResult
