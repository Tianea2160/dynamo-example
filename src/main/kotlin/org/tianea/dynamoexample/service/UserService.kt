package org.tianea.dynamoexample.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.tianea.dynamoexample.model.User
import org.tianea.dynamoexample.repository.UserRepository
import java.util.*

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {
    fun createUser(name: String, email: String, age: Int): User {
        val user = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email,
            age = age
        )
        return userRepository.save(user)
    }

    fun getUser(id: String): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun searchUsersByName(name: String): List<User> {
        return userRepository.findByNameContaining(name)
    }

    fun updateUser(id: String, name: String?, email: String?, age: Int?): User? {
        val existingUser = userRepository.findById(id).orElse(null) ?: return null
        
        name?.let { existingUser.name = it }
        email?.let { existingUser.email = it }
        age?.let { existingUser.age = it }
        
        return userRepository.save(existingUser)
    }

    fun deleteUser(id: String): Boolean {
        if (!userRepository.existsById(id)) return false
        userRepository.deleteById(id)
        return true
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll().toList()
    }
} 