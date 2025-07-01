package org.linkparser.model

import java.net.URI

data class GitHubResult(var url: URI, var user: String, var repo: String) : LinkParserResult
