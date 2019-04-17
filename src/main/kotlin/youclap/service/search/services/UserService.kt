package youclap.service.search.services

import org.elasticsearch.common.unit.Fuzziness
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.MultiMatchQueryBuilder
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilders
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Component
import youclap.service.search.controllers.SearchUserController
import youclap.service.search.models.PrivacyType
import youclap.service.search.models.User
import youclap.service.search.repositories.UserRepository
import kotlin.random.Random

@Component
class UserService {

    val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    lateinit var userRepository: UserRepository

    val names = arrayOf("Abigail", "Alexandra", "Alison", "Amanda", "Amelia", "Amy", "Andrea", "Angela", "Anna", "Anne", "Audrey", "Ava", "Bella", "Bernadette", "Carol", "Caroline", "Carolyn", "Chloe", "Claire", "Deirdre", "Diana", "Diane", "Donna", "Dorothy", "Elizabeth", "Ella", "Emily", "Emma", "Faith", "Felicity", "Fiona", "Gabrielle", "Grace", "Hannah", "Heather", "Irene", "Jan", "Jane", "Jasmine", "Jennifer", "Jessica", "Joan", "Joanne", "Julia", "Karen", "Katherine", "Kimberly", "Kylie", "Lauren", "Leah", "Lillian", "Lily", "Lisa", "Madeleine", "Maria", "Mary", "Megan", "Melanie", "Michelle", "Molly", "Natalie", "Nicola", "Olivia", "Penelope", "Pippa", "Rachel", "Rebecca", "Rose", "Ruth", "Sally", "Samantha", "Sarah", "Sonia", "Sophie", "Stephanie", "Sue", "Theresa", "Tracey", "Una", "Vanessa", "Victoria", "Virginia", "Wanda", "Wendy", "Yvonne", "Nicholas", "Dominic")
    val usernames = arrayOf("Paul Tucker", "Gabrielle Turner", "Andrew Hill", "Irene Rees", "Peter Miller", "Benjamin Lyman", "Brian Young", "Nicholas Edmunds", "Cameron Dowd", "Austin Cameron", "Trevor Hudson", "Yvonne Piper", "Chloe Miller", "Peter Scott", "Heather Fisher", "Keith Bell", "Gavin Ross", "Paul Simpson", "Max Walsh", "Molly Davies", "Tracey Morrison", "William Hart", "Jonathan Walsh", "Nicola Gray", "Adrian Edmunds", "Claire Ellison", "Simon Paige", "Molly Hemmings", "Yvonne Burgess", "Lillian Glover", "Ruth Lyman", "Christopher Reid", "Madeleine Duncan", "Maria Knox", "Jack Bower", "Gordon MacLeod", "Anna Hughes", "Jake Manning", "Jasmine Wilkins", "Anne Gibson", "Simon Alsop", "Alison White", "Olivia Fisher", "Phil Sutherland", "Victoria Anderson", "Alexander Ball", "Andrea Peters", "Sue Chapman", "Dominic Watson", "Nicholas Davidson", "Jacob Bower", "Caroline Sharp", "Max Lawrence", "Trevor Martin", "Bernadette Paterson", "Trevor Reid", "Leah Welch", "Sophie Terry", "Pippa Ball", "Lauren Manning", "Joe MacDonald", "Edward Randall", "Keith Butler", "Connor Young", "Ella Alsop", "Vanessa Quinn", "Anthony Lyman", "Anne Kerr", "Donna Stewart", "Jane Taylor", "Heather Murray", "Bernadette Mitchell", "Kylie Chapman", "Joe Sutherland", "Jonathan Wilkins", "Neil Vaughan", "Edward Hughes", "Tim Gill", "Sonia Berry", "Victor Hodges", "Evan Russell", "Dominic Hamilton", "Irene Martin", "Jack Paige", "Jan Miller", "Nathan Young", "Warren Lambert", "Jasmine Mitchell", "Rose Slater", "Michelle Stewart", "Lucas Reid", "Molly Skinner", "Matt Newman", "Tim Buckland", "Justin Avery", "Joe Coleman", "Dominic Henderson", "Jennifer Robertson", "Carol Langdon", "Lily Turner", "Amelia Harris", "Sonia Jackson", "Eric Berry", "Connor Marshall", "Zoe Quinn", "William Randall", "Ryan Duncan", "David Paige", "Eric Newman", "Stewart Terry", "Ian Hardacre", "Mary Dickens", "Jasmine Hemmings", "Sonia King", "Julian Stewart", "Dorothy Rampling", "Carol Buckland", "Kevin Edmunds")
    val bananas = List(100) { Random.nextInt(1, 100) }


    fun getAllUsers(): MutableIterable<User> {
        return userRepository.findAll(PageRequest.of(0, 10))
    }

    fun getUserByBananas(bananas: Double): User {
        return userRepository.findUserByBananas(bananas)
    }

    fun insertUser(user: User): User {
        return userRepository.save(user)
    }

    fun insertBulkUsers(number: Int): String {
        for (i: Int in 1..number) {
            val randomID = Random.nextInt().toString()
            val randomNamePosition = Random.nextInt(names.size)
            val randomUsernamePosition = Random.nextInt(usernames.size)
            val randomBananasPosition = Random.nextInt(bananas.size)
            val randomUser = User(randomID, names[randomNamePosition], usernames[randomUsernamePosition], bananas[randomBananasPosition].toDouble(), false)
            userRepository.save(randomUser)
        }
        return "inserted bulk users"
    }

    fun getUsersByName(name: String): Page<User> {
        logger.info("get users by name $name")
        return userRepository.findByNameUsingCustomQuery(name, PageRequest.of(0, 20))
    }

    fun getUsersByDeleted(deleted: Boolean): Page<User> {
        logger.info("get deleted users ")
        return userRepository.findUsersByDeletedUsingCustomQuery1(deleted, PageRequest.of(0, 10))
    }

    fun getUsers(text: String): Page<User> {
        var searchQuery = NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(text)
                        .field("username", 2.0F)
                        .field("name")
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
                        .operator(Operator.OR)
                        .fuzziness(Fuzziness.AUTO)
                )
                .withFilter(QueryBuilders.boolQuery()
                        .mustNot(QueryBuilders.matchQuery("deleted", true))
                ).build()

        return userRepository.search(searchQuery)
    }
}