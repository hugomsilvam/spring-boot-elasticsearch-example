package youclap.service.search.builders

import org.elasticsearch.action.search.SearchRequestBuilder
import org.elasticsearch.client.Client
import org.elasticsearch.index.query.QueryBuilders
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import youclap.service.search.config.ElasticSearchConfig
import youclap.service.search.models.SearchIndex
import youclap.service.search.models.UserFields

class SearchUserBuilder(
        private val index: SearchIndex = SearchIndex.USER,
        private val client: Client = ElasticSearchConfig().createElasticTransportClient(),
        var query: String
) {

    private val logger: Logger = LoggerFactory.getLogger(SearchUserBuilder::class.java)

    fun createUserSearchBuilder(): SearchRequestBuilder {
        val searchBuilder: SearchRequestBuilder = client.prepareSearch(index.name.toLowerCase())
                .setQuery(QueryBuilders.queryStringQuery(query)
                        .field(UserFields.USERNAME.name.toLowerCase(), 2.0f)
                        .field(UserFields.NAME.name.toLowerCase())
                )
                .setPostFilter(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery(UserFields.DELETED.name.toLowerCase(), false))
                )
        logger.info("user search builder $searchBuilder")
        return searchBuilder
    }
}