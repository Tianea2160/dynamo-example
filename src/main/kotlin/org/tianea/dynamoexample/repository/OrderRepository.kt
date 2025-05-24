package org.tianea.dynamoexample.repository

import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository
import org.tianea.dynamoexample.model.Order
import java.time.LocalDateTime

@EnableScan
interface OrderRepository : DynamoDBCrudRepository<Order, String> {
    // Find orders by user ID
    fun findByUserId(userId: String): List<Order>
    
    // Find orders by status using GSI
    fun findByStatusIndex(status: String): List<Order>
    
    // Find orders by status and date range using GSI
    fun findByStatusIndexAndOrderDateIndexBetween(
        status: String,
        startDate: String,
        endDate: String
    ): List<Order>
    
    // Find orders by user ID and date range using composite key
    fun findByUserIdAndOrderIdBetween(
        userId: String,
        startOrderId: String,
        endOrderId: String
    ): List<Order>
} 