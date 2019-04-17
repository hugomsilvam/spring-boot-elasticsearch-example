package youclap.service.search.services

import org.elasticsearch.index.query.MultiMatchQueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Component
import youclap.service.search.models.Challenge
import youclap.service.search.models.PrivacyType
import youclap.service.search.repositories.ChallengeRepository
import kotlin.random.Random

@Component
class ChallengeService {

    val logger: Logger = LoggerFactory.getLogger(ChallengeService::class.java)

    @Autowired
    lateinit var challengeRepository: ChallengeRepository

    val titles = arrayListOf("Guest Of The Crash", "Clone Of Death", "Pilots With Tentacles", "Intruders With A Spaceship", "Emperors And Cyborgs", "Mercenaries And Doctors", "Rise Of The Vacuum", "Ascension Of New Worlds", "Frozen By The Titans", "Demand For Space Flight", "Mime Program", "Girl Of Gags", "Cat Makeover", "Spider Strategy", "Rebel And Lord", "Officer And Lord", "Traps Delusion", "Stunts From The Forests", "Confusion Of My Intentions", "Simplicity Of That Girl", "Cat Of Dreams", "Queen Of The Great", "Slaves Of The Frontline", "Pirates Of History", "Foes And Snakes", "Bandits And Friends", "Result Of Utopia", "Inspiration Without Sin", "Fighting The Fires", "Invited By My Destiny")
    val descriptions = arrayListOf("Yeah, I think it's a good environment for learning English.", "I was very proud of my nickname throughout high school but today- I couldn’t be any different to what my nickname was.", "Christmas is coming.", "Let me help you with your baggage.", "Tom got a small piece of pie.", "Check back tomorrow; I will see if the book has arrived.", "I checked to make sure that he was still alive.", "I hear that Nancy is very pretty.", "She advised him to come back at once.", "She did her best to help him.", "She was too short to see over the fence.", "Abstraction is often one floor above you.", "I'd rather be a bird than a fish.", "A glittering gem is not enough.", "If the Easter Bunny and the Tooth Fairy had babies would they take your teeth and leave chocolate for you?", "The lake is a long way from here.", "The quick brown fox jumps over the lazy dog.", "I currently have 4 windows open up… and I don’t know why.", "How was the math test?", "She folded her handkerchief neatly.", "A song can make or ruin a person’s day if they let it get to them.", "The body may perhaps compensates for the loss of a true metaphysics.", "She borrowed the book from him many years ago and hasn't yet returned it.", "Please wait outside of the house.", "Should we start class now, or should we wait for everyone to get here?", "Someone I know recently combined Maple Syrup & buttered Popcorn thinking it would taste like caramel popcorn.", "It didn’t and they don’t recommend anyone else do it either.", "The mysterious diary records the voice.", "I hear that Nancy is very pretty.", "If I don’t like something, I’ll stay away from it.", "Yeah, I think it's a good environment for learning English.", "She was too short to see over the fence.", "I am happy to take your donation; any amount will be greatly appreciated.", "I'd rather be a bird than a fish.", "The quick brown fox jumps over the lazy dog.", "She wrote him a long letter, but he didn't read it.", "If you like tuna and tomato sauce- try combining the two. It’s really not as bad as it sounds.", "Malls are great places to shop; I can find everything I need under one roof.", "Wednesday is hump day, but has anyone asked the camel if he’s happy about it?", "The lake is a long way from here.", "I am never at home on Sundays.")

    fun insertChallenge(challenge: Challenge): Challenge {
        return challengeRepository.save(challenge)
    }

    fun insertBulkChallenges(number: Int) {
        for (i: Int in 1..number) {
            val randomId = Random.nextInt().toString()
            val randomTitlePosition = Random.nextInt(titles.size)
            val randomDescriptionPosition = Random.nextInt(descriptions.size)
            val randomDay = Random.nextInt(28) + 1
            val randomMonth = Random.nextInt(12) + 1
            val randomDateTimeBegin = DateTime(2019, randomMonth, randomDay, 0, 0)
            val randomDateTimeEnd = randomDateTimeBegin.plusDays(randomDay)
            val challenge = Challenge(randomId, titles[randomTitlePosition], descriptions[randomDescriptionPosition], randomChallengePrivacy(), randomDeletedChallenge(), randomDateTimeBegin.toDate(), randomDateTimeEnd.toDate())
            challengeRepository.save(challenge)
        }
    }

    fun randomChallengePrivacy(): PrivacyType {
        val random = Random.nextInt(100)
        if (random < 75) {
            return PrivacyType.PUBLIC
        } else if (random < 90) {
            return PrivacyType.PRIVATE_GROUP
        } else {
            return PrivacyType.PRIVATE_USER
        }
    }

    fun randomDeletedChallenge(): Boolean {
        val random = Random.nextInt(100)
        if (random < 85) {
            return false
        }
        return true

    }

    fun searchChallengeByTitle(title: String): Page<Challenge> {
        return challengeRepository.findByTitle(title, PageRequest.of(0, 10))
    }

    fun searchChallenges(query: String): Page<Challenge> {
        var searchQuery = NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(query)
                        .field("title", 2.0F)
                        .field("description")
                        .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                )
                .withFilter(QueryBuilders.boolQuery()
                        .mustNot(QueryBuilders.matchQuery("deleted", true))
                        .must(QueryBuilders.matchQuery("privacy", PrivacyType.PUBLIC.name))
                )
                .build()

        return challengeRepository.search(searchQuery)
    }
}