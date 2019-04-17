package youclap.service.search.builders

import org.elasticsearch.action.search.SearchRequestBuilder
import org.elasticsearch.client.Client
import org.elasticsearch.index.query.QueryBuilders
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import youclap.service.search.config.ElasticSearchConfig
import youclap.service.search.models.ChallengeFields
import youclap.service.search.models.PrivacyType
import youclap.service.search.models.SearchIndex

class SearchChallengeBuilder(
        private val index: SearchIndex = SearchIndex.CHALLENGE,
        private val client: Client = ElasticSearchConfig().createElasticTransportClient(),
        var query: String
) {

    private val logger: Logger = LoggerFactory.getLogger(SearchChallengeBuilder::class.java)

    fun createChallengeSearchBuilder(): SearchRequestBuilder {
        val searchBuilder: SearchRequestBuilder = client.prepareSearch(index.name.toLowerCase())
                .setQuery(QueryBuilders.queryStringQuery(query)
                        .field(ChallengeFields.TITLE.name.toLowerCase(), 2.0f)
                        .field(ChallengeFields.DESCRIPTION.name.toLowerCase())
                )
                .setPostFilter(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery(ChallengeFields.DELETED.name.toLowerCase(), false))
                        .must(QueryBuilders.matchQuery(ChallengeFields.PRIVACY.name.toLowerCase(), PrivacyType.PUBLIC.name))
                )
        logger.info("challenge search builder $searchBuilder")
        return searchBuilder
    }
}