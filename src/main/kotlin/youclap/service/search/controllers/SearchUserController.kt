package youclap.service.search.controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import youclap.service.search.models.User
import youclap.service.search.services.UserService
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/users"])
class SearchUserController {

    val logger: Logger = LoggerFactory.getLogger(SearchUserController::class.java)

    @Autowired
    lateinit var userService: UserService

    @GetMapping
    fun getValidUsers(@RequestParam(value = "q") query: String): Page<User> {
        logger.info("get valid users query $query")
        return userService.getUsers(query)
    }

    @GetMapping(value = ["all"])
    fun getAllUsers(): MutableIterable<User> {
        logger.info("get all users")
        return userService.getAllUsers()
    }

    @PostMapping
    fun insertUser(@RequestBody @Valid user: User): User {
        logger.info("insert user ", user)
        userService.insertUser(user)
        return user
    }

    @GetMapping(value = ["bulk/{number}"])
    @ResponseBody
    fun insertBulkUsers(@PathVariable(required = false) number: Int): String {
        logger.info("insert bulk users number=($number)")
        return userService.insertBulkUsers(number)
    }

    @GetMapping(value = ["usersB"])
    fun getUserByBananas(@RequestParam(name = "bananas") bananas: Double): String {
        logger.info("get user by bananas = $bananas")
        return userService.getUserByBananas(bananas).toString()
    }

    @GetMapping(value = ["name/{name}"])
    fun getUsersByName(@PathVariable name: String): Page<User> {
        logger.info("controller get users by name $name")
        return userService.getUsersByName(name)
    }

    @GetMapping(value = ["deleted/{deleted}"])
    fun getUsersByDeleted(@PathVariable deleted: Boolean): Page<User> {
        logger.info("controller get users by deleted $deleted")
        return userService.getUsersByDeleted(deleted)
    }

}