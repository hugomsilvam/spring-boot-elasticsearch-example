package youclap.service.search.services

import youclap.service.search.models.Challenge
import youclap.service.search.models.SearchIndex
import youclap.service.search.models.SearchResults
import youclap.service.search.models.User

interface SearchService {

    fun searchUsers(query: String): List<User>
    fun searchChallenges(query: String): List<Challenge>
    fun searchAll(query: String): SearchResults
}