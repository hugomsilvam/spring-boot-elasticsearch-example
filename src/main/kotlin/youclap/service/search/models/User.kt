package youclap.service.search.models

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.elasticsearch.common.Nullable
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import java.util.*

@Document(indexName = "user", type = "user")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class User(
        @Id
        @JsonProperty("id")
        var id: String,

        @JsonProperty("name")
        var name: String,

        @JsonProperty("username")
        var username: String,

        @JsonProperty("bananas")
        var bananas: Double,

        @JsonProperty("deleted")
        var deleted: Boolean
        )