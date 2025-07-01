package org.linkparser.service

import lombok.Setter
import org.linkparser.model.LinkParserResult
import java.net.URI

@Setter
abstract class LinkParser {

    var nextParser: LinkParser? = null

    abstract fun parseUrl(url: URI): LinkParserResult?

    protected fun parseNext(url: URI): LinkParserResult? {
        return nextParser?.parseUrl(url)
    }
}