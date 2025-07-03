package org.linkparser

import org.junit.jupiter.api.Test
import org.linkparser.model.GitHubResult
import org.linkparser.model.StackOverflowResult
import org.linkparser.service.LinkParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.net.URI
import java.net.URISyntaxException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
class LinkParserApplicationTests {

    @Autowired
    private val linkParser: LinkParser? = null

    @Test
    @Throws(URISyntaxException::class)
    fun parseValidGithubLink() {
        val uri = URI("https://github.com/fedinairn08/scrapper")

        val expected = GitHubResult(uri, "fedinairn08", "scrapper")
        val actual = linkParser!!.parseUrl(uri) as GitHubResult

        assertNotNull(actual)
        assertEquals(expected.user, actual.user)
        assertEquals(expected.repo, actual.repo)
    }

    @Test
    @Throws(URISyntaxException::class)
    fun parseValidStackOverflowLink() {
        val uri = URI("https://stackoverflow.com/questions/927358/how-do-i-undo-the-most-recent-local-commits-in-git")

        val expected = StackOverflowResult(uri, 927358L)
        val actual = linkParser!!.parseUrl(uri) as StackOverflowResult

        assertNotNull(actual)
        assertEquals(expected.questionId, actual.questionId)
    }

    @Test
    @Throws(URISyntaxException::class)
    fun parseInvalidGithubLink() {
        val uri = URI("https://github.com/delete")

        assertNull(linkParser!!.parseUrl(uri))
    }

    @Test
    @Throws(URISyntaxException::class)
    fun parseInvalidStackOverflowLink() {
        val uri = URI("https://stackoverflow.com/")

        assertNull(linkParser!!.parseUrl(uri))
    }
}
