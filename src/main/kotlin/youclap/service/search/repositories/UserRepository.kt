package youclap.service.search.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import youclap.service.search.models.User

@Repository
interface UserRepository : ElasticsearchRepository<User, String> {

    @Query("{\"bool\":{\"must\": [{\"match\": {\"name\": \"?0\"}}]}}") //this query annotation forces the query to match with the field
    fun findByNameUsingCustomQuery(name: String, pageable: Pageable) : Page<User>

    @Query("{\"bool\":{\"filter\": [{\"term\": {\"deleted\": ?1}}]}}")
    fun findUsersByDeletedUsingCustomQuery1(deleted: Boolean, pageable: Pageable): Page<User>

    fun findUserByBananas(bananas: Double) : User
}
