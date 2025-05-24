package org.tianea.dynamoexample.repository

import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository
import org.tianea.dynamoexample.model.User

@EnableScan
interface UserRepository : DynamoDBCrudRepository<User, String> {
    // Spring Data DynamoDB will automatically implement basic CRUD operations
    
    // Custom query methods can be added here
    fun findByEmail(email: String): User?
    fun findByNameContaining(name: String): List<User>
} 