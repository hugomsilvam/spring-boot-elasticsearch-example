package youclap.service.search.controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import youclap.service.search.models.Challenge
import youclap.service.search.services.ChallengeService

@RequestMapping(value = ["/challenges"])
@RestController
class SearchChallengeController {

    val logger: Logger = LoggerFactory.getLogger(SearchChallengeController::class.java)

    @Autowired
    lateinit var challengeService: ChallengeService

    @GetMapping
    fun searchChallengesByQuery(@RequestParam(value = "q") query: String): Page<Challenge> {
        logger.info("challenge controller search query $query")
        return challengeService.searchChallenges(query)
    }

    @GetMapping(value = ["title/{title}"])
    fun searchChallenesByTitle(@PathVariable title: String): Page<Challenge> {
        logger.info("search challenges by title $title")
        return challengeService.searchChallengeByTitle(title)
    }

    @GetMapping(value = ["bulk/{number}"])
    fun addBulkChallenges(@PathVariable number: Int) {
        challengeService.insertBulkChallenges(number)
    }

    @PostMapping
    fun addChallenge(@RequestBody challenge: Challenge): Challenge {
        return challengeService.insertChallenge(challenge)
    }

    @GetMapping(value = ["all"])
    fun listChallenges(): Page<Challenge> {
        return challengeService.challengeRepository.findAll(PageRequest.of(0, 20))
    }
}