package org.tianea.dynamoexample.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.tianea.dynamoexample.model.Order
import org.tianea.dynamoexample.model.OrderItem
import org.tianea.dynamoexample.model.OrderStatus
import org.tianea.dynamoexample.repository.OrderRepository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository
) {
    fun createOrder(userId: String, items: List<OrderItem>): Order {
        val orderDate = LocalDateTime.now()
        val order = Order(
            userId = userId,
            orderId = generateOrderId(orderDate),
            orderDate = orderDate,
            items = items,
            totalAmount = calculateTotal(items),
            status = OrderStatus.PENDING,
            statusIndex = OrderStatus.PENDING.name,
            orderDateIndex = orderDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
        return orderRepository.save(order)
    }

    fun getOrdersByUser(userId: String): List<Order> {
        return orderRepository.findByUserId(userId)
    }

    fun getOrdersByStatus(status: OrderStatus): List<Order> {
        return orderRepository.findByStatusIndex(status.name)
    }

    fun getOrdersByStatusAndDateRange(
        status: OrderStatus,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Order> {
        return orderRepository.findByStatusIndexAndOrderDateIndexBetween(
            status = status.name,
            startDate = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            endDate = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }

    fun getUserOrdersByDateRange(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Order> {
        return orderRepository.findByUserIdAndOrderIdBetween(
            userId = userId,
            startOrderId = generateOrderId(startDate),
            endOrderId = generateOrderId(endDate)
        )
    }

    fun updateOrderStatus(userId: String, orderId: String, newStatus: OrderStatus): Order? {
        val order = orderRepository.findByUserIdAndOrderIdBetween(userId, orderId, orderId)
            .firstOrNull() ?: return null

        order.status = newStatus
        order.statusIndex = newStatus.name
        return orderRepository.save(order)
    }

    private fun generateOrderId(dateTime: LocalDateTime): String {
        val timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val random = UUID.randomUUID().toString().substring(0, 8)
        return "$timestamp-$random"
    }

    private fun calculateTotal(items: List<OrderItem>): BigDecimal {
        return items.fold(BigDecimal.ZERO) { acc, item ->
            acc + (item.price * BigDecimal(item.quantity))
        }
    }
} 