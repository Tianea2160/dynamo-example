package org.tianea.dynamoexample.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tianea.dynamoexample.model.User
import org.tianea.dynamoexample.service.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<User> {
        val user = userService.createUser(
            name = request.name,
            email = request.email,
            age = request.age
        )
        return ResponseEntity.ok(user)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<User> {
        return userService.getUser(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<User> {
        return userService.getUserByEmail(email)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/search")
    fun searchUsers(@RequestParam name: String): ResponseEntity<List<User>> {
        val users = userService.searchUsersByName(name)
        return ResponseEntity.ok(users)
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: String,
        @RequestBody request: UpdateUserRequest
    ): ResponseEntity<User> {
        return userService.updateUser(
            id = id,
            name = request.name,
            email = request.email,
            age = request.age
        )?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Unit> {
        return if (userService.deleteUser(id)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

data class CreateUserRequest(
    val name: String,
    val email: String,
    val age: Int
)

data class UpdateUserRequest(
    val name: String? = null,
    val email: String? = null,
    val age: Int? = null
) 