package youclap.service.search.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import java.util.*

@Document(indexName = "challenge", type = "challenge")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Challenge(
        @Id
        @JsonProperty("id")
        var id: String,

        @JsonProperty("title")
        var title: String,

        @JsonProperty("description")
        var description: String,

        @JsonProperty("privacy")
        var privacy: PrivacyType,

        @JsonProperty("deleted")
        var deleted: Boolean,

        @JsonProperty("startDate")
        var startDate: Date,

        @JsonProperty("endDate")
        var endDate: Date
)