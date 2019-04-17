package youclap.service.search.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import youclap.service.search.models.Challenge

@Repository
interface ChallengeRepository : ElasticsearchRepository<Challenge, String> {

    fun findByTitle(title: String, pageable: Pageable): Page<Challenge>
}