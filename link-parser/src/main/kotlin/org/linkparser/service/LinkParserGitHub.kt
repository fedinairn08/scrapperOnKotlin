package org.linkparser.service

import org.linkparser.model.GitHubResult
import org.linkparser.model.LinkParserResult
import java.net.URI
import java.util.regex.Pattern

class LinkParserGitHub : LinkParser() {

    private val githubPattern: Pattern = Pattern.compile("^/([^/]+)/([^/]+)/?$")

    override fun parseUrl(url: URI): LinkParserResult? {
        if (url.host == "github.com") {
            val path = url.path
            val matcher = githubPattern.matcher(path)
            if (matcher.matches()) {
                return GitHubResult(url, matcher.group(1), matcher.group(2))
            }
        }
        return parseNext(url)
    }
}