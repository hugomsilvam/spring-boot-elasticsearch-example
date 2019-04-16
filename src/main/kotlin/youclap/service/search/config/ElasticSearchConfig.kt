package youclap.service.search.config

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import java.net.InetAddress

@Configuration
@EnableElasticsearchRepositories(basePackages = ["youclap.service.search.repositories"])
@ComponentScan(basePackages = ["youclap.service.search.services"])
class ElasticSearchConfig {

    var elasticsearchHome: String = "/usr/share/elasticsearch"

    var clusterName: String = "docker-cluster"

    fun createElasticTransportClient () : Client {
        var elasticsearchSettings = Settings.builder()
                .put("client.transport.sniff", true)
                .put("path.home", elasticsearchHome)
                .put("cluster.name", clusterName).build()
        var client : TransportClient = PreBuiltTransportClient(elasticsearchSettings)
        client.addTransportAddress(TransportAddress(InetAddress.getByName("127.0.0.1"), 9300))
        return client
    }

    @Bean
    fun elasticsearchTemplate() : ElasticsearchOperations {
        return ElasticsearchTemplate(createElasticTransportClient())
    }
}