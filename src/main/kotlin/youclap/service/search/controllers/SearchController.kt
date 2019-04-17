package youclap.service.search.controllers


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import youclap.service.search.exceptions.IndexBadRequestException
import youclap.service.search.models.SearchIndex
import youclap.service.search.models.SearchResults
import youclap.service.search.services.SearchService

@RequestMapping(value = ["/search"])
@RestController
class SearchController {

    val logger: Logger = LoggerFactory.getLogger(SearchController::class.java)

    @Autowired
    lateinit var searchService: SearchService

    @GetMapping
    fun search(@RequestParam(value = "query") query: String, @RequestParam(value = "index", required = false) indexes: List<SearchIndex>?): SearchResults {
        logger.info("search query $query on indexes $indexes")

        if (indexes.isNullOrEmpty() || (indexes.contains(SearchIndex.USER) && indexes.contains(SearchIndex.CHALLENGE))) {
            return searchService.searchAll(query)
        } else if (indexes.contains(SearchIndex.USER)) {
            val usersResults = searchService.searchUsers(query)
            return SearchResults(userSearchResults = usersResults, query = query)
        } else if (indexes.contains(SearchIndex.CHALLENGE)) {
            val challengeResults = searchService.searchChallenges(query)
            return SearchResults(challengeSearchResults = challengeResults, query = query)
        } else {
            logger.info("deu merda - so para despistar")
            throw IndexBadRequestException() //nunca entra neste else
        }
    }
}