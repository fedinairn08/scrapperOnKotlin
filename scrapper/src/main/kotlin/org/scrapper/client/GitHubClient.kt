package org.scrapper.client

import lombok.RequiredArgsConstructor
import org.scrapper.dto.response.GitHubRepositoryResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.util.regex.Pattern

@Component
@RequiredArgsConstructor
class GitHubClient(
    private val gitHubRestClient: RestClient,

    @Value("\${api.github.token}")
    private var githubToken: String
    ) {

    fun getGitHubRepositoryInfo(username: String, repositoryName: String): GitHubRepositoryResponse? {
        return gitHubRestClient.get()
                .uri("/repos/{username}/{repositoryName}", username, repositoryName)
                .retrieve()
                .body(GitHubRepositoryResponse::class.java)
    }

    fun getCommitCount(username: String, repositoryName: String): Int {
        val response = gitHubRestClient.get()
            .uri("/repos/{username}/{repositoryName}/commits?per_page=1", username, repositoryName)
            .retrieve()
            .toBodilessEntity()

        return extractCommitCountFromHeaders(response.headers)
    }

    fun getBranchCount(username: String, repositoryName: String): Int {
        val response = gitHubRestClient.get()
            .uri("/repos/{username}/{repositoryName}/branches?per_page=1", username, repositoryName)
            .retrieve()
            .toBodilessEntity()

        return extractBranchCountFromHeaders(response.headers)
    }

    private fun extractBranchCountFromHeaders(headers: HttpHeaders): Int {
        val linkHeader = headers.getFirst(HttpHeaders.LINK)
        if (linkHeader != null) {
            val pattern = Pattern.compile("page=(\\d+)>; rel=\"last\"")
            val matcher = pattern.matcher(linkHeader)
            if (matcher.find()) {
                return matcher.group(1).toInt()
            }
        }
        return 1
    }

    private fun extractCommitCountFromHeaders(headers: HttpHeaders): Int {
        val linkHeader = headers.getFirst(HttpHeaders.LINK)
        val pattern = Pattern.compile("page=(\\d+)>; rel=\"last\"")
        val matcher = pattern.matcher(linkHeader)
        if (matcher.find()) {
            return matcher.group(1).toInt()
        }

        return getCommitCountFromBody(headers)
    }

    private fun getCommitCountFromBody(headers: HttpHeaders): Int {
        val path = headers.getFirst("X-GitHub-Request-Id")

        val commits = gitHubRestClient.get()
            .uri(path!!)
            .retrieve()
            .body(List::class.java)

        return commits?.size ?: 0
    }
}