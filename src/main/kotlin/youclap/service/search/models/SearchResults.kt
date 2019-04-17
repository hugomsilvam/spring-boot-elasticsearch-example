package youclap.service.search.models

data class SearchResults(
        val userSearchResults: List<User> = listOf(),
        val challengeSearchResults: List<Challenge> = listOf(),
        val query: String
) {

}