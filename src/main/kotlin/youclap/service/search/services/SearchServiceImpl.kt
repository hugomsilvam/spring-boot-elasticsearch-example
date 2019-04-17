package youclap.service.search.services

import org.elasticsearch.action.search.MultiSearchRequestBuilder
import org.elasticsearch.action.search.MultiSearchResponse
import org.elasticsearch.client.Client
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import youclap.service.search.builders.SearchChallengeBuilder
import youclap.service.search.builders.SearchUserBuilder
import youclap.service.search.config.ElasticSearchConfig
import youclap.service.search.models.Challenge
import youclap.service.search.models.SearchIndex
import youclap.service.search.models.SearchResults
import youclap.service.search.models.User
import kotlin.math.absoluteValue

@Component
class SearchServiceImpl : SearchService {

    //    val context: ApplicationContext = AnnotationConfigApplicationContext(ElasticSearchConfig::class.java)
//    val config = context.getBean("search", ElasticSearchConfig::class.java)
//
//    @Autowired
//    val client: Client = config.createElasticTransportClient()
    val client: Client = ElasticSearchConfig().createElasticTransportClient()

    val logger: Logger = LoggerFactory.getLogger(SearchServiceImpl::class.java)

    override fun searchUsers(query: String): List<User> {
        logger.info("search only users query $query")
        return emptyList()
    }

    override fun searchChallenges(query: String): List<Challenge> {
        logger.info("search only challenges query $query")
        return emptyList()
    }

    override fun searchAll(query: String): SearchResults {
        val usersResult = searchUsers(query)
        val challengesResult = searchChallenges(query)
        return SearchResults(usersResult, challengesResult, query)
    }

    fun searchES(query: String): Array<Object> {
        val challengeSearchRequestBuilder = SearchChallengeBuilder(query = query).createChallengeSearchBuilder()

        val userSearchRequestBuilder = SearchUserBuilder(query = query).createUserSearchBuilder()

        val searchResponse: MultiSearchResponse = client.prepareMultiSearch()
                .add(challengeSearchRequestBuilder)
                .add(userSearchRequestBuilder)
                .get()

        for (item: MultiSearchResponse.Item in searchResponse.responses) {
            logger.info("response item ${item.response} number of hits ${item.response.hits.getTotalHits().absoluteValue}")
        }
        return emptyArray()
    }

    fun searchES(query: String, indexes: List<SearchIndex>): Array<Object> {
        logger.info("search by specific index $indexes")
        var searchRequestBuilder: MultiSearchRequestBuilder = client.prepareMultiSearch()
        for (index in indexes) {
            if (index == SearchIndex.CHALLENGE) {
                val challengeSearchRequestBuilder = SearchChallengeBuilder(query = query).createChallengeSearchBuilder()
                searchRequestBuilder.add(challengeSearchRequestBuilder)
            } else if (index == SearchIndex.USER) {
                val userSearchRequestBuilder = SearchUserBuilder(query = query).createUserSearchBuilder()
                searchRequestBuilder.add(userSearchRequestBuilder)
            }
        }

        val searchResponse: MultiSearchResponse = searchRequestBuilder.get()
        for (item: MultiSearchResponse.Item in searchResponse.responses) {
            logger.info("response item ${item.response} number of hits ${item.response.hits.getTotalHits().absoluteValue}")
        }

        return emptyArray()
    }
}