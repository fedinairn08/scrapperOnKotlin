package org.scrapper.client

import lombok.RequiredArgsConstructor
import org.scrapper.dto.response.GitHubRepositoryResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
@RequiredArgsConstructor
class GitHubClient(private val gitHubRestClient: RestClient) {

    fun getGitHubRepositoryInfo(username: String, repositoryName: String): GitHubRepositoryResponse? {
        return gitHubRestClient.get()
                .uri("/repos/{username}/{repositoryName}", username, repositoryName)
                .retrieve()
                .body(GitHubRepositoryResponse::class.java)
    }
}