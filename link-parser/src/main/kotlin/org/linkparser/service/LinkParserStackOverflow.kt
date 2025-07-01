package org.linkparser.service

import java.util.regex.Pattern
import org.linkparser.model.LinkParserResult
import org.linkparser.model.StackOverflowResult
import org.springframework.stereotype.Component
import java.net.URI

@Component
class LinkParserStackOverflow : LinkParser() {

    private val stackOverflowPattern: Pattern = Pattern.compile("^/questions/(\\d+)/.*$")

    override fun parseUrl(url: URI): LinkParserResult? {
        if (url.host == "stackoverflow.com") {
            val path = url.path
            val matcher = stackOverflowPattern.matcher(path)
            if (matcher.matches()) {
                return StackOverflowResult(url, matcher.group(1).toLong())
            }
        }
        return parseNext(url)
    }
}